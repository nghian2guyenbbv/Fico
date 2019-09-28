package vn.com.tpf.microservices.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PGPHelper {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final int BUFFER_SIZE = 1 << 16;

	private char[] password;
	private PGPSecretKey secretKey;
	private PGPPublicKey encryptionPublicKey;
	private PGPPublicKey signaturePublicKey;
	private PGPSecretKeyRingCollection pgpSec;
	private PGPSignatureGenerator signatureGenerator;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public PGPHelper(String project) {
		try {
			password = PGPInfo.preshareKey.toCharArray();
			InputStream priStream = new ByteArrayInputStream(PGPInfo.privateKey.getBytes());
			InputStream pubStream = new ByteArrayInputStream(PGPInfo.publicKey.get(project).getBytes());
			readKey(pubStream, priStream, null, null);
		} catch (Exception e) {
			log.info("PGPHelper " + e.getMessage());
		}
	}

	private void readKey(InputStream pubStream, InputStream priStream, Long encryptPublicKeyId, Long signaturePublicKeyId)
			throws Exception {
		readPublicKey(pubStream, encryptPublicKeyId, signaturePublicKeyId);
		pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(priStream));
		secretKey = readSecretKey(pgpSec);
	}

	private void readPublicKey(InputStream in, Long encryptPublicKeyId, Long signaturePublicKeyId) throws Exception {
		in = PGPUtil.getDecoderStream(in);
		PGPPublicKeyRingCollection pkCol = new PGPPublicKeyRingCollection(in);
		PGPPublicKeyRing pkRing;
		Iterator<?> it = pkCol.getKeyRings();
		if (encryptPublicKeyId == null || encryptPublicKeyId == -1) {
			while (it.hasNext()) {
				pkRing = (PGPPublicKeyRing) it.next();
				Iterator<?> pkIt = pkRing.getPublicKeys();
				while (pkIt.hasNext()) {
					PGPPublicKey key = (PGPPublicKey) pkIt.next();
					if (key.isEncryptionKey()) {
						encryptionPublicKey = key;
						break;
					}
				}
			}
		} else {
			encryptionPublicKey = pkCol.getPublicKey(encryptPublicKeyId);
		}

		if (encryptionPublicKey == null) {
			throw new PGPException("Invalid public Key");
		}

		if (signaturePublicKeyId == null || signaturePublicKeyId == -1) {
			signaturePublicKey = encryptionPublicKey;
		} else {
			signaturePublicKey = pkCol.getPublicKey(signaturePublicKeyId);
		}
	}

	private PGPSecretKey readSecretKey(PGPSecretKeyRingCollection collection) throws Exception {
		Iterator<?> it = collection.getKeyRings();
		PGPSecretKeyRing pbr;
		while (it.hasNext()) {
			Object readData = it.next();
			if (readData instanceof PGPSecretKeyRing) {
				pbr = (PGPSecretKeyRing) readData;
				return pbr.getSecretKey();
			}
		}
		throw new IllegalArgumentException("secret key for message not found.");
	}

	private PGPSignatureGenerator createSignatureGenerator() throws Exception {
		if (signatureGenerator == null) {
			PGPPrivateKey pgpPrivKey = secretKey.extractPrivateKey(password, "BC");
			PGPPublicKey internalPublicKey = secretKey.getPublicKey();
			PGPSignatureGenerator generator = new PGPSignatureGenerator(internalPublicKey.getAlgorithm(),
					HashAlgorithmTags.SHA1, "BC");
			generator.initSign(PGPSignature.BINARY_DOCUMENT, pgpPrivKey);
			for (Iterator<?> i = internalPublicKey.getUserIDs(); i.hasNext();) {
				String userId = (String) i.next();
				PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
				spGen.setSignerUserID(false, userId);
				generator.setHashedSubpackets(spGen.generate());
				break;
			}
			signatureGenerator = generator;
		}
		return signatureGenerator;
	}

	private PGPPrivateKey findSecretKey(long keyID) throws Exception {
		PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);
		if (pgpSecKey == null) {
			return null;
		}
		return pgpSecKey.extractPrivateKey(password, "BC");
	}

	private void writeToLiteralData(PGPSignatureGenerator signatureGenerator, OutputStream out, byte[] data)
			throws Exception {
		PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
		ByteArrayInputStream contentStream = new ByteArrayInputStream(data);
		try (OutputStream literalOut = lData.open(out, PGPLiteralData.BINARY, "pgp", new Date(), new byte[BUFFER_SIZE])) {
			byte[] buf = new byte[BUFFER_SIZE];
			int len;
			while ((len = contentStream.read(buf, 0, buf.length)) > 0) {
				literalOut.write(buf, 0, len);
				signatureGenerator.update(buf, 0, len);
			}
		} finally {
			lData.close();
		}
	}

	public void decryptAndVerifySignature(BufferedReader encryptBufferedReader, OutputStream decryptData) {
		try {
			byte[] encryptData = encryptBufferedReader.toString().getBytes();
			InputStream bais = PGPUtil.getDecoderStream(new ByteArrayInputStream(encryptData));
			PGPObjectFactory objectFactory = new PGPObjectFactory(bais);

			Object firstObject = objectFactory.nextObject();
			PGPEncryptedDataList dataList = (PGPEncryptedDataList) (firstObject instanceof PGPEncryptedDataList ? firstObject
					: objectFactory.nextObject());

			if (dataList == null) {
				throw new PGPException("encrypt data not correct");
			}

			Iterator<?> it = dataList.getEncryptedDataObjects();
			PGPPrivateKey privateKey = null;
			PGPPublicKeyEncryptedData encryptedData = null;

			while (privateKey == null && it.hasNext()) {
				encryptedData = (PGPPublicKeyEncryptedData) it.next();
				privateKey = findSecretKey(encryptedData.getKeyID());
			}

			if (encryptedData == null || privateKey == null) {
				throw new IllegalArgumentException("secret key for message not found.");
			}

			InputStream clear = encryptedData.getDataStream(privateKey, "BC");
			PGPObjectFactory clearObjectFactory = new PGPObjectFactory(clear);
			Object message = clearObjectFactory.nextObject();

			if (message instanceof PGPCompressedData) {
				PGPCompressedData cData = (PGPCompressedData) message;
				objectFactory = new PGPObjectFactory(cData.getDataStream());
				message = objectFactory.nextObject();
			}

			PGPOnePassSignature calculatedSignature = null;
			if (message instanceof PGPOnePassSignatureList) {
				calculatedSignature = ((PGPOnePassSignatureList) message).get(0);
				calculatedSignature.initVerify(signaturePublicKey, "BC");
				message = objectFactory.nextObject();
			}

			if (message instanceof PGPLiteralData) {
				PGPLiteralData ld = (PGPLiteralData) message;
				InputStream literalDataStream = ld.getInputStream();
				int ch;
				while ((ch = literalDataStream.read()) >= 0) {
					if (calculatedSignature != null) {
						calculatedSignature.update((byte) ch);
					}
					decryptData.write((byte) ch);
				}
			} else if (message instanceof PGPOnePassSignatureList) {
				throw new PGPException("encrypted message contains a signed message - not literal data.");
			} else {
				throw new PGPException("message is not a simple encrypted file - type unknown.");
			}

			if (calculatedSignature != null) {
				PGPSignatureList signatureList = (PGPSignatureList) objectFactory.nextObject();
				PGPSignature messageSignature = (PGPSignature) signatureList.get(0);
				if (!calculatedSignature.verify(messageSignature)) {
					throw new PGPException("signature verification failed");
				}
			}

			if (encryptedData.isIntegrityProtected()) {
				if (!encryptedData.verify()) {
					throw new PGPException("message failed integrity check");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void encryptAndSign(byte[] data, OutputStream out) {
		try {
			out = new ArmoredOutputStream(out);
			PGPEncryptedDataGenerator encrypted = new PGPEncryptedDataGenerator(PGPEncryptedDataGenerator.CAST5,
					new SecureRandom(), "BC");
			encrypted.addMethod(encryptionPublicKey);
			PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedDataGenerator.ZIP);
			try (OutputStream encryptedOut = encrypted.open(out, new byte[BUFFER_SIZE])) {
				try (OutputStream compressedOut = comData.open(encryptedOut)) {
					PGPSignatureGenerator pgpsg = createSignatureGenerator();
					pgpsg.generateOnePassVersion(false).encode(compressedOut);
					writeToLiteralData(pgpsg, compressedOut, data);
					pgpsg.generate().encode(compressedOut);
				}
			} finally {
				encrypted.close();
				comData.close();
				out.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}

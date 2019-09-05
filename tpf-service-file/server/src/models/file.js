const mongoose = require('mongoose')

const schema = new mongoose.Schema({
	filename: { type: String, required: true },
	length: { type: Number, required: true },
	chunkSize: { type: Number, required: true },
	md5: { type: String, required: true },
	contentType: { type: String, required: true },
	uploadDate: { type: Date, default: Date.now },
	aliases: { type: Object }
})

module.exports = mongoose.model('fs.files', schema)
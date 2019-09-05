const { MongoClient, GridFSBucket } = require('mongodb')
const GridFsStorage = require('multer-gridfs-storage')
const multer = require('multer')
const mongoose = require('mongoose')

class Storage {

	constructor() {
		const promise = MongoClient
			.connect(process.env.MONGO_URI, { useNewUrlParser: true })
			.then(client => {
				const db = client.db(process.env.MONGO_DATABASE)
				this.bucket = new GridFSBucket(db)
				return db
			})

		this.upload = multer({
			storage: new GridFsStorage({
				db: promise, cache: true,
				file: (req, file) => {
					const id = new mongoose.Types.ObjectId()
					return { id, filename: id + '-' + file.originalname, aliases: { used: false } }
				}
			})
		})
	}

}

module.exports = new Storage()

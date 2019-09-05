const express = require('express')
const router = express.Router()
const { Storage } = require('../helpers')
const { FileModel } = require('../models')

router.post('/v1/file', Storage.upload.array('file'), async (req, res, next) => {
	if (!req.files) return next({ status: 404, message: 'No File Upload' })
	res.send(req.files)
})

router.get('/v1/file/:filename', async (req, res, next) => {
	const file = await FileModel.findOne({ filename: req.params.filename })
	if (!file) return next({ status: 404, message: 'Not Found' })
	res.set('Content-Type', file.contentType)
	Storage.bucket.openDownloadStream(file._id).pipe(res)
})

router.delete('/v1/file/:filename', async (req, res, next) => {
	const file = await FileModel.findOne({ filename: req.params.filename })
	if (!file) return next({ status: 404, message: 'Not Found' })
	Storage.bucket.delete(file._id).catch(console.error)
	res.send()
})

module.exports = router
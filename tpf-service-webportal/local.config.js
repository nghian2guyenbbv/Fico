module.exports = {
	apps: [
		{
			name: 'server',
			cwd: './server',
			script: 'npm start',
		},
		{
			name: 'client',
			cwd: './client',
			script: 'npm start'
		}
	]
}
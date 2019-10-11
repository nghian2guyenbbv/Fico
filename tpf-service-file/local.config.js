module.exports = {
	apps: [
		{
			name: 'server',
			cwd: './server',
			script: 'npm start',
			env: {
				NODE_ENV: 'local'
			}
		}
	]
}
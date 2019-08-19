module.exports = {
	apps: [
		{
			name: 'server',
			cwd: './server',
			script: 'npm start',
			env: {
				NODE_ENV: 'local'
			}
		},
		{
			name: 'client',
			cwd: './client',
			script: 'npm start'
		}
	]
}
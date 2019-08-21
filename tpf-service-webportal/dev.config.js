module.exports = {
	apps: [
		{
			name: 'server',
			cwd: './server',
			script: './src/main.js',
			instances: 1,
			exec_mode: 'cluster',
			env: {
				NODE_ENV: 'dev'
			}
		}
	]
}
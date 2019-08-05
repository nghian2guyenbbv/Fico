module.exports = {
  apps: [
    {
      name: 'server',
      cwd: './server',
      script: './src/main.js',
      instances: 2,
      exec_mode: 'cluster'
    }
  ]
}
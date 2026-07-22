const http = require('http')
const fs = require('fs')
const path = require('path')
const httpProxy = require('http-proxy')

const PORT = 8081
const DIST_DIR = path.join(__dirname, 'dist')
const BACKEND = 'http://localhost:8080'

const mimeTypes = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.gif': 'image/gif',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
  '.woff': 'font/woff',
  '.woff2': 'font/woff2',
  '.ttf': 'font/ttf',
  '.eot': 'application/vnd.ms-fontobject',
  '.map': 'application/json'
}

const proxy = httpProxy.createProxyServer({ target: BACKEND, changeOrigin: true })

const server = http.createServer((req, res) => {
  // Proxy API requests to backend
  if (req.url.startsWith('/dev-api')) {
    req.url = req.url.replace('/dev-api', '')
    proxy.web(req, res)
    return
  }

  // Remove query parameters from URL
  let urlPath = req.url.split('?')[0]
  // Decode URI and normalize
  urlPath = decodeURIComponent(urlPath)

  // Serve static files from dist
  let filePath = path.join(DIST_DIR, urlPath === '/' ? 'index.html' : urlPath)

  const ext = path.extname(filePath)
  fs.readFile(filePath, (err, data) => {
    if (err) {
      // If request has a file extension, return 404 (don't serve HTML for missing JS/CSS)
      if (ext) {
        res.writeHead(404, { 'Content-Type': 'text/plain' })
        res.end('Not Found')
      } else {
        // SPA fallback: serve index.html for unknown routes (no file extension)
        fs.readFile(path.join(DIST_DIR, 'index.html'), (e2, d2) => {
          if (e2) {
            res.writeHead(404)
            res.end('Not Found')
          } else {
            res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' })
            res.end(d2)
          }
        })
      }
    } else {
      res.writeHead(200, { 'Content-Type': mimeTypes[ext] || 'application/octet-stream' })
      res.end(data)
    }
  })
})

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running at http://localhost:${PORT}/`)
})

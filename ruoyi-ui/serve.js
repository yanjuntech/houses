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

function encodeUrlPath(url) {
  const qIndex = url.indexOf('?')
  if (qIndex === -1) {
    return encodeURI(url)
  }
  const path = url.substring(0, qIndex)
  const query = url.substring(qIndex + 1)
  const encodedPairs = query.split('&').map(pair => {
    const eqIndex = pair.indexOf('=')
    if (eqIndex === -1) {
      return encodeURIComponent(pair)
    }
    const key = pair.substring(0, eqIndex)
    const value = pair.substring(eqIndex + 1)
    return encodeURIComponent(key) + '=' + encodeURIComponent(value)
  }).join('&')
  return encodeURI(path) + '?' + encodedPairs
}

const server = http.createServer((req, res) => {
  if (req.url.startsWith('/dev-api')) {
    req.url = req.url.replace('/dev-api', '')
    proxy.web(req, res)
    return
  }

  let urlPath = req.url.split('?')[0]
  urlPath = decodeURIComponent(urlPath)

  let filePath = path.join(DIST_DIR, urlPath === '/' ? 'index.html' : urlPath)

  const ext = path.extname(filePath)
  fs.readFile(filePath, (err, data) => {
    if (err) {
      if (ext) {
        res.writeHead(404, { 'Content-Type': 'text/plain' })
        res.end('Not Found')
      } else {
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

server.on('connection', (socket) => {
  let buffer = Buffer.alloc(0)
  let headersParsed = false

  const originalEmit = socket.emit
  socket.emit = function(eventName, ...args) {
    if (eventName === 'data' && !headersParsed) {
      const chunk = args[0]
      buffer = Buffer.concat([buffer, chunk])

      const headerEnd = buffer.indexOf('\r\n\r\n')
      if (headerEnd !== -1) {
        headersParsed = true
        const headerSection = buffer.slice(0, headerEnd).toString('utf8')
        const lines = headerSection.split('\r\n')
        if (lines.length > 0) {
          const requestLine = lines[0]
          const parts = requestLine.split(' ')
          if (parts.length >= 2) {
            const method = parts[0]
            const rawUrl = parts[1]
            const version = parts[2] || 'HTTP/1.1'

            const hasNonAscii = /[^\x00-\x7F]/.test(rawUrl)
            if (hasNonAscii) {
              const encodedUrl = encodeUrlPath(rawUrl)
              lines[0] = method + ' ' + encodedUrl + ' ' + version
              const newHeaderSection = lines.join('\r\n')
              const newBuffer = Buffer.concat([
                Buffer.from(newHeaderSection, 'utf8'),
                Buffer.from('\r\n\r\n', 'utf8'),
                buffer.slice(headerEnd + 4)
              ])
              buffer = newBuffer
            }
          }
        }

        const bodyData = buffer.slice(headerEnd + 4)
        if (bodyData.length > 0) {
          originalEmit.call(socket, 'data', buffer)
        } else {
          originalEmit.call(socket, 'data', buffer.slice(0, headerEnd + 4))
        }
        buffer = null
        return true
      }

      if (buffer.length > 8192) {
        headersParsed = true
        originalEmit.call(socket, 'data', buffer)
        buffer = null
        return true
      }

      return true
    }
    return originalEmit.apply(this, arguments)
  }
})

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running at http://localhost:${PORT}/`)
})

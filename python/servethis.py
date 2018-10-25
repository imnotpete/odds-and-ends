import sys
import http.server
import socketserver

# minimal web server.  serves files relative to the
# current directory.

port = 8100

if len(sys.argv) == 2:
        port = int(sys.argv[1])

handler = http.server.SimpleHTTPRequestHandler

httpd = socketserver.TCPServer(("", port), handler)

print("serving at port", port, flush=True)

sys.stdout.flush()

httpd.serve_forever()

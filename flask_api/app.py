from flask import Flask, jsonify, request

app = Flask(__name__)

### Excepciones con ficheros ###
@app.route('/api/file-not-found', methods=['GET'])
def missing_file_error():
    return (jsonify({'message': 'error'}), 200)

@app.route('/api/file-open-error', methods=['GET'])
def open_file_error():
    return (jsonify({'message': 'error'}), 200)

@app.route('/api/file-read-error', methods=['GET'])
def read_file_error():
    return (jsonify({'message': 'error'}), 200)

@app.route('/api/file-write-error', methods=['GET'])
def write_file_error():
    return (jsonify({'message': 'error'}), 200)

### Excepciones en el uso de APIs de terceros ###

if __name__ == '__main__':
    # Se lanza la API en el localhost con el puerto 5000
    app.run(host='0.0.0.0', port=5000, debug=True, passthrough_errors=True)
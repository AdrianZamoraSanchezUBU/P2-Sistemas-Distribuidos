from flask import Flask, jsonify, request
import os

app = Flask(__name__)

### Excepciones con ficheros ###
@app.route('/api/file/list', methods=['GET'])
def file_list():
    try:
        path = './files/'
        files = os.listdir(path)
        
        files.append("file4.txt")

        return jsonify({
            "files": files
        }), 200
    except Exception as e:
        print(e)

        return jsonify({
            "error": {
                "code": "UNKNOWN_ERROR",
                "message": str(e)
            }
        }), 500

@app.route('/api/file/read', methods=['GET'])
def read_file():

    filename = request.args.get('filename')

    if not filename:
        return jsonify({
            "error": {
                "code": "MISSING_FILENAME",
                "message": "Se requiere el nombre del archivo"
            }
        }), 400

    path = os.path.join('./files', filename)

    # Se trata de abrir y mostrar el contenido
    try:
        with open(path, 'r') as f:
            content = f.read()
        return jsonify({
            "filename": filename,
            "content": content
        }), 200
    
    # Gestión de excepciones
    except FileNotFoundError:
        return jsonify({
            "error": {
                "code": "FILE_NOT_FOUND",
                "message": f"El archivo: '{filename}' no existe"
            }
        }), 404
    except PermissionError:
        return jsonify({
            "error": {
                "code": "PERMISSION_DENIED",
                "message": f"No tienes permisos para leer el fichero: '{filename}'."
            }
        }), 403
    except Exception as e:
        return jsonify({
            "error": {
                "code": "FILE_READ_ERROR",
                "message": str(e)
            }
        }), 500

@app.route('/api/file/write', methods=['GET'])
def write_file():

    filename = request.args.get('filename')

    if not filename:
        return jsonify({
            "error": {
                "code": "MISSING_FILENAME",
                "message": "Se requiere el nombre del archivo"
            }
        }), 400

    path = os.path.join('./files', filename)

    # Se trata de leer el contenido (un número) y sumar uno
    try:
        with open(path, 'r+') as f:
            content = f.read().strip()

            try:
                number = int(content)
            except ValueError:
                return jsonify({
                    "error": {
                        "code": "INVALID_CONTENT",
                        "message": f"No se puede escribir en el archivo: '{filename}, puesto que no contiene un número'"
                    }
                }), 400

            number += 1
            f.seek(0)
            f.write(str(number))
            f.truncate()
        return jsonify({
            "filename": filename,
            "content": number
        }), 200
    
    # Gestión de excepciones
    except FileNotFoundError:
        return jsonify({
            "error": {
                "code": "FILE_NOT_FOUND",
                "message": f"El archivo: '{filename}' no existe"
            }
        }), 404
    except PermissionError:
        return jsonify({
            "error": {
                "code": "PERMISSION_DENIED",
                "message": f"No tienes permisos para leer el fichero: '{filename}'."
            }
        }), 403
    except Exception as e:
        return jsonify({
            "error": {
                "code": "FILE_READ_ERROR",
                "message": str(e)
            }
        }), 500

### Excepciones en el uso de APIs de terceros ###

if __name__ == '__main__':
    # Se lanza la API en el localhost con el puerto 5000
    app.run(host='0.0.0.0', port=5000, debug=True, passthrough_errors=True)
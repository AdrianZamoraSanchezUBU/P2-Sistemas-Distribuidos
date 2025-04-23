from flask import Flask, jsonify, request
import mysql.connector
from mysql.connector import Error, InterfaceError, OperationalError
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
            "response": {
                "code": "UNKNOWN_ERROR",
                "message": str(e)
            }
        }), 500

@app.route('/api/file/read', methods=['GET'])
def read_file():

    filename = request.args.get('filename')

    if not filename:
        return jsonify({
            "response": {
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
            "response": {
                "code": "FILE_NOT_FOUND",
                "message": f"El archivo: '{filename}' no existe"
            }
        }), 404
    except PermissionError:
        return jsonify({
            "response": {
                "code": "PERMISSION_DENIED",
                "message": f"No tienes permisos para leer el fichero: '{filename}'."
            }
        }), 403
    except Exception as e:
        return jsonify({
            "response": {
                "code": "FILE_READ_ERROR",
                "message": str(e)
            }
        }), 500

@app.route('/api/file/write', methods=['GET'])
def write_file():

    filename = request.args.get('filename')

    if not filename:
        return jsonify({
            "response": {
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
                    "response": {
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
            "response": {
                "code": "FILE_NOT_FOUND",
                "message": f"El archivo: '{filename}' no existe"
            }
        }), 404
    except PermissionError:
        return jsonify({
            "response": {
                "code": "PERMISSION_DENIED",
                "message": f"No tienes permisos para leer el fichero: '{filename}'."
            }
        }), 403
    except Exception as e:
        return jsonify({
            "response": {
                "code": "FILE_READ_ERROR",
                "message": str(e)
            }
        }), 500

### Excepciones en el uso de bases de datos ###
def get_connection(db):
    return mysql.connector.connect(
        host="mysqldb",
        port=3306,
        user="appuser",
        password="apppass",
        database= db
    )

def table_check(cursor, db, table):
    cursor.execute("""
        SELECT COUNT(*)
        FROM information_schema.tables
        WHERE table_schema = %s AND table_name = %s
    """, (db, table))
    return cursor.fetchone()[0] == 1

@app.route('/api/db/connect')
def test_db_connection():
    db = request.args.get('db')
    table = request.args.get('table')

    if not db or not table:
        return jsonify({
            "response": {
                "code": "MISSING_PARAMETERS",
                "message": "Se requieren parámetros de conexión correctos"
            }
        }), 400

    try:
        conn = get_connection(db)
        cursor = conn.cursor()

        if not table_check(cursor, db, table):
            return jsonify({
                "response": {
                    "code": "TABLE_NOT_FOUND",
                    "message": f"La tabla '{table}' no existe en la base de datos '{db}'."
                }
            }), 404

        # Una query que prueba que existe y devuelve la cantidad de filas
        cursor.execute(f"SELECT COUNT(*) FROM {table}")
        count = cursor.fetchone()[0]

        return jsonify({
            "response": {
                "code": "CONNECTION_SUCCESSFUL",
                "db": db,
                "table": table,
                "message": f"Conectado a la base de datos: {db}, tabla: {table} con {count} filas"
            }
        }), 200

    except OperationalError as e:
        return jsonify({
            "response": {
                "code": "CONNECTION_ERROR",
                "message": f"No se pudo conectar: {str(e)}"
            }
        }), 500
    except Error as e:
        return jsonify({
            "response": {
                "code": "MYSQL_ERROR",
                "message": f"Error de MySQL: {str(e)}"
            }
        }), 500
    finally:
        try:
            cursor.close()
            conn.close()
        except:
            pass

@app.route('/api/db/recurso-inexistente')
def missing_resource_error():
    try:
        db = request.args.get('db')
        table = request.args.get('table')

        conn = get_connection(db)
        cursor = conn.cursor()

        # Se selecciona de una tabla inexistente
        cursor.execute(f"SELECT * FROM no_existe")
    except Error as e:
        return jsonify({
            "response": {
                "code": "QUERY_ERROR",
                "message": f"Error de query: {str(e)}"
            }
        }), 404
    finally:
        try:
            cursor.close()
            conn.close()
        except:
            pass

@app.route('/api/db/error-insercion')
def insert_error():
    try:
        db = request.args.get('db')
        table = request.args.get('table')

        conn = get_connection(db)
        cursor = conn.cursor()

        # Inserción con clave primaria duplicada (ya debe haber un admin)
        cursor.execute(f"INSERT INTO {table} (username, password) VALUES ('admin', 'pass')")
        conn.commit()
    except Error as e:
        return jsonify({
            "response": {
                "code": "MYSQL_ERROR",
                "message": f"Error de inserción: {str(e)}"
            }
        }), 409
    finally:
        try:
            cursor.close()
            conn.close()
        except:
            pass

@app.route('/api/db/formato-incorrecto')
def bad_format_error():
    try:
        db = request.args.get('db')
        table = request.args.get('table')

        conn = get_connection(db)
        cursor = conn.cursor()

        # Se inserta un numero en campo string 1 en campo username
        cursor.execute(f"INSERT INTO {table} (id, username, password) VALUES ('string', 'usuarioErroneo', 'pass')")
        conn.commit()
    except Error as e:
        return jsonify({
            "response": {
                "code": "QUERY_ERROR",
                "message": f"Error de formato: {str(e)}"
            }
        }), 409
    finally:
        try:
            cursor.close()
            conn.close()
        except:
            pass

@app.route('/api/db/query-error')
def sql_syntax_error():
    try:
        db = request.args.get('db')
        table = request.args.get('table')

        conn = get_connection(db)
        cursor = conn.cursor()

        # Mal escrito
        cursor.execute(f"SELECCIONA * FROM {table}")
    except Error as e:
        return jsonify({
            "response": {
                "code": "QUERY_ERROR",
                "message": f"Error de query: {str(e)}"
            }
        }), 400
    finally:
        try:
            cursor.close()
            conn.close()
        except:
            pass

### Excepciones en el uso de APIs de terceros ###


if __name__ == '__main__':
    # Se lanza la API en el localhost con el puerto 5000
    app.run(host='0.0.0.0', port=5000, debug=True, passthrough_errors=True)
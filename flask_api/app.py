# Imports necesarios
from flask import Flask, jsonify, request
import mysql.connector
from mysql.connector import Error, InterfaceError, OperationalError
import os
import requests

app = Flask(__name__)

### Excepciones con ficheros ###
@app.route('/api/file/list', methods=['GET'])
def file_list():
    """
    Devuelve un JSON con los archivos de la ruta ./files/

    En caso de excepción devuelve un JSON con el motivo de la misma
    """
    try:
        # Se listan los archivos
        path = './files/'
        files = os.listdir(path)
        
        files.append("file4.txt")

        # Devuelve la lista y un código de éxito
        return jsonify({
            "files": files
        }), 200
    except Exception as e:
        # Devuelve un error y el motivo
        return jsonify({
            "response": {
                "code": "UNKNOWN_ERROR",
                "message": str(e)
            }
        }), 500

@app.route('/api/file/read', methods=['GET'])
def read_file():
    """
    Devuelve un JSON con el contenido del fichero especificado
    por el argumento filename

    En caso de excepción devuelve un JSON con el motivo de la misma
    """

    filename = request.args.get('filename')

    # Comprueba que se especifica un archivo
    if not filename:
        # En caso de faltar el parámetro se genera un error controlado
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

        # Devuelve el nombre del fichero y el contenido leido en él
        return jsonify({
            "filename": filename,
            "content": content
        }), 200
    
    # Gestión de las excepciones identificadas 
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
    """
    Devuelve un JSON con el resultado de sumar 1 al contenido
    del fichero especificado como argumento filename

    En caso de excepción devuelve un JSON con el motivo de la misma
    """

    filename = request.args.get('filename')

    # Comprueba que se especifica un archivo
    if not filename:
        # En caso de faltar el parámetro se genera un error controlado
        return jsonify({
            "response": {
                "code": "MISSING_PARAMETER",
                "message": "Se requiere el nombre del archivo"
            }
        }), 4004

    path = os.path.join('./files', filename)

    # Se trata de leer el contenido (un número) y sumar uno
    try:
        with open(path, 'r+') as f:
            content = f.read().strip()

            # Se comprueba que el formato se puede pasar a integer
            try:
                number = int(content)
            except ValueError:
                # Si el formato no era correcto devuelve un error controlado
                return jsonify({
                    "response": {
                        "code": "INVALID_CONTENT",
                        "message": f"No se puede escribir en el archivo: '{filename}, puesto que no contiene un número'"
                    }
                }), 400

            # Suma 1 al contenido y lo sobreescribe 
            number += 1
            f.seek(0)
            f.write(str(number))
            f.truncate()

        # Devuelve el fichero y su nuevo contenido
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
    """
    Crea una conexión con la base de datos pasada como parámetros

    Parámetros:
        db: base de datos a la que se desea conectar
    """
    return mysql.connector.connect(
        host="mysqldb",
        port=3306,
        user="appuser",
        password="apppass",
        database= db
    )

def table_check(cursor, db, table):
    """
    Comrprueba que se puede utilizar la tabla, devuelve un conteo del
    numero de entradas presentes en la tabla

    Parámetros:
        cursor: objeto de MySQL donde se especifica la query
        db:     base de datos a la que se le hace la query
        table:  tabla sobre la que se hace la query
    """

    cursor.execute("""
        SELECT COUNT(*)
        FROM information_schema.tables
        WHERE table_schema = %s AND table_name = %s
    """, (db, table))
    return cursor.fetchone()[0] == 1

@app.route('/api/db/connect')
def test_db_connection():
    """
    Gestiona la realización de conexiones
    """

    db = request.args.get('db')
    table = request.args.get('table')

    # Si falta un parámetro deuvelve un error controlado
    if not db or not table:
        return jsonify({
            "response": {
                "code": "MISSING_PARAMETERS",
                "message": "Se requieren parámetros de conexión correctos"
            }
        }), 400

    try:
        # Conecta con la base de datos
        conn = get_connection(db)
        cursor = conn.cursor()

        # Hace una comprobación sobre la tabla
        if not table_check(cursor, db, table):
            # Devuelve un error de tabla no existente si no se puede realizar con éxito
            return jsonify({
                "response": {
                    "code": "TABLE_NOT_FOUND",
                    "message": f"La tabla '{table}' no existe en la base de datos '{db}'."
                }
            }), 404

        # Una query que prueba que existe y devuelve la cantidad de filas
        cursor.execute(f"SELECT COUNT(*) FROM {table}")
        count = cursor.fetchone()[0]

        # Devuelve el resultado con un código de éxtio
        return jsonify({
            "response": {
                "code": "CONNECTION_SUCCESSFUL",
                "db": db,
                "table": table,
                "message": f"Conectado a la base de datos: {db}, tabla: {table} con {count} filas"
            }
        }), 200
    
    # Se gestionan y devuelven las distintas excepciones identificadas
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
    """
    Comprobación de la falta de un recurso al que se trata de acceder
    """
    try:
        db = request.args.get('db')

        conn = get_connection(db)
        cursor = conn.cursor()

        # Se selecciona de una tabla inexistente
        cursor.execute(f"SELECT * FROM no_existe")
    except Error as e:
        # Se espera lanzar una excepción con un código 404 (recurso no existente)
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
    """
    Gestión de errores durante la inserción de elementos en la base de datos
    """
    # Se trata de hacer una conexión
    try:
        db = request.args.get('db')
        table = request.args.get('table')

        conn = get_connection(db)
        cursor = conn.cursor()

        # Inserción con clave primaria duplicada (ya debe haber un admin)
        cursor.execute(f"INSERT INTO {table} (username, password) VALUES ('admin', 'pass')")
        conn.commit()
    # En caso de error durante la inserción se devuelve una excepción y su motivo
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
    """
    Comprobación del error que sucede al incumplir intencionalmente el formato de un campo
    """
    try:
        db = request.args.get('db')
        table = request.args.get('table')

        conn = get_connection(db)
        cursor = conn.cursor()

        # Se inserta un numero en campo string 1 en campo username
        cursor.execute(f"INSERT INTO {table} (id, username, password) VALUES ('string', 'usuarioErroneo', 'pass')")
        conn.commit()
    except Error as e:
        # En caso de error se devuelve un error y su motivo
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
    """
    Se trata de hacer una query pero con formato incorrecto
    """
    # Se realiza una conexión y query
    try:
        db = request.args.get('db')
        table = request.args.get('table')

        conn = get_connection(db)
        cursor = conn.cursor()

        # Mal escrito
        cursor.execute(f"SELECCIONA * FROM {table}")
    except Error as e:
        # En caso de error durante la query se devuelve una excepción y su motivo
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
@app.route('/api/pokemon/query-pokemon')
def query_pokemon():
    """
    Se trata de hacer una solicitud a la API para buscar información de un Pokemon
    """
    name = request.args.get('name')
    request_url = f"https://pokeapi.co/api/v2/pokemon/{name}"


    try:
        response = requests.get(request_url, timeout=5)
        response.raise_for_status() # Lanzará HTTPError si la respuesta no es 2xx

        data = response.json()

        # Devuelve los datos del Pokemon en caso de éxito
        return jsonify({
            "response": {
                "code": "POKEMON_FOUND",
                "message": "Datos de " + name + " obtenidos con éxito",
                "name": data["name"],
                "id": data["id"],
                "weight": data["weight"],
                "height": data["height"],
                "image_url": data["sprites"]["front_default"]
            }
        }), 200

    # En caso de errores se devuelve el código de respuesta y el motivo cuando se aposible
    except requests.exceptions.HTTPError as e:
        return jsonify({
            "response": {
                "code": "ERROR",
                "message": f"Error HTTP: {response.status_code}"
            }
        }), response.status_code

    except requests.exceptions.ConnectionError:
        return jsonify({
            "response": {
                "code": "CONNECTION_ERROR",
                "message": "No se pudo conectar con PokéAPI"
            }
        }), 503

    except requests.exceptions.Timeout:
        return jsonify({
            "response": {
                "code": "TIMEOUT",
                "message": "La API de Pokemon ha tardado demasiado en responder"
            }
        }), 504

    except requests.exceptions.RequestException as e:
        return jsonify({
            "response": {
                "code": "UNKNOWN_REQUEST_ERROR",
                "message": f"Error: {str(e)}"
            }
        }), 500

if __name__ == '__main__':
    # Se lanza la API en el localhost con el puerto 5000
    app.run(host='0.0.0.0', port=5000, debug=True, passthrough_errors=True)
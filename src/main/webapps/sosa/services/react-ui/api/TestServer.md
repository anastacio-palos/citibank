## JSON Server

JSON server is used for simulating response from backend service.

- Configuration : db.json db.json file contains the uri and the json response.

- routes: routes.json routes.json is used for mapping additional routes.

# Starting the server

json-server ./api/db.json --routes ./api/routes.json --port 4000

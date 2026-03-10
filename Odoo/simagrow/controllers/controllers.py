# -*- coding: utf-8 -*-
import json
import os
from odoo import http
from odoo.modules import get_module_resource

class Simagrow(http.Controller):
    @http.route('/simagrow/usuarios', auth='public', methods=['GET'], type='http')
    def get_usuarios(self, **kwargs):
        usuarios = http.request.env['simagrow.usuario'].sudo().search([])
        users_data = []
        for u in usuarios:
            users_data.append({
                'id': u.id,
                'nif': u.nif,
                'nombre': u.nombre,
                'apellidos': u.apellidos,
                'fecha_nacimiento': str(u.fecha_nacimiento) if u.fecha_nacimiento else None,
                'correo': u.correo,
                'contrasena': u.contrasena,
                'creditos': u.creditos,
                'num_incidencias': u.num_incidencias,
                'name': u.name,
            })
        
        return http.Response(
            json.dumps(users_data),
            status=200,
            mimetype='application/json'
        )

    @http.route('/simagrow/usuario/create', auth='public', methods=['GET', 'POST', 'OPTIONS'], type='http', csrf=False, cors='*')
    def create_usuario(self, **kwargs):
        if http.request.httprequest.method == 'GET':
            
            path = get_module_resource('simagrow', 'cliente_web_usuarios.html')
            if path and os.path.exists(path):
                with open(path, 'r', encoding='utf-8') as f:
                    content = f.read()
                return http.Response(content, mimetype='text/html', status=200)
            return http.Response("Archivo HTML no encontrado", status=404)

        try:
            data = json.loads(http.request.httprequest.data)
            
            nuevo_usuario = http.request.env['simagrow.usuario'].sudo().create({
                'nif': data.get('nif'),
                'nombre': data.get('nombre'),
                'apellidos': data.get('apellidos'),
                'fecha_nacimiento': data.get('fecha_nacimiento'),
                'admin': data.get('admin', False),
                'creditos': 0,
            })
            
            return http.Response(
                json.dumps({'status': 'success', 'id': nuevo_usuario.id}),
                status=200,
                mimetype='application/json'
            )
        except Exception as e:
            return http.Response(
                json.dumps({'status': 'error', 'error': str(e)}),
                status=400,
                mimetype='application/json'
            )

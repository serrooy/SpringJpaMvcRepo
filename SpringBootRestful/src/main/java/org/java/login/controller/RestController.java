package org.java.login.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.java.login.service.MainServices;
import org.java.login.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestController {

	@Autowired
	MainServices mainService;

	/**
	 * Consulta todos los usuarios de la base de datos
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllUser", method = { RequestMethod.GET })
	public List<String> getAllUser() throws Exception {
		try {
			return mainService.consultaAllUser();
		} catch (SQLException e) {
			throw new SQLException("Se ha producido un error al eliminar");
		}

	}

	/**
	 * Inserta un nuevo usuario
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/register", method = { RequestMethod.POST })
	public String newUser(@RequestParam Map<String, String> requestParams) throws Exception {
		if (requestParams.get(Constantes.USERNAME) != null && requestParams.get(Constantes.PASS) != null) {
			String user = requestParams.get(Constantes.USERNAME);
			String pass = requestParams.get(Constantes.PASS);
			try {
				mainService.insertUser(user, pass);
			} catch (SQLException e) {
				throw new SQLException("Se ha producido un error al insertar");
			}
		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return "EL usuario ['" + requestParams.get(Constantes.USERNAME) + "'] ha sido registrado correctamente";
	}

	/**
	 * Actualiza la password de un usuario
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePass", method = { RequestMethod.PUT }) // PATCH PARA MOD PARCIALES
	public String updatePass(@RequestParam Map<String, String> requestParams) throws Exception {
		if (requestParams.get(Constantes.USERNAME) != null && requestParams.get(Constantes.PASS) != null) {
			String user = requestParams.get(Constantes.USERNAME);
			String pass = requestParams.get(Constantes.PASS);
			try {
				mainService.updatePass(user, pass);
			} catch (SQLException e) {
				throw new SQLException("Se ha producido un error al actualizar");
			}
		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return "La contraseña del usuario ['" + requestParams.get(Constantes.USERNAME)
				+ "'] ha sido modificada correctamente";
	}

	/**
	 * Elimina un usuario y su log de conexiones
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delUser", method = { RequestMethod.DELETE })
	public String delUser(@RequestParam Map<String, String> requestParams) throws Exception {
		if (requestParams.get(Constantes.USERNAME) != null) {
			String user = requestParams.get(Constantes.USERNAME);
			try {
				mainService.deleteUser(user);
			} catch (SQLException e) {
				throw new SQLException("Se ha producido un error al eliminar");
			}
		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return "EL usuario ['" + requestParams.get(Constantes.USERNAME) + "'] ha sido eliminado correctamente";
	}

	@ResponseBody
	@RequestMapping(value = "/deleteAllRows", method = { RequestMethod.DELETE })
	public String delAll(@RequestParam Map<String, String> requestParams) throws Exception {
		// TODO
		return "EL usuario ['" + requestParams.get(Constantes.USERNAME) + "'] ha sido eliminado correctamente";
	}

}

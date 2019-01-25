package org.java.login.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.java.login.model.LogLine;
import org.java.login.model.User;
import org.java.login.repository.LogLineDao;
import org.java.login.repository.UserDao;
import org.java.login.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestJpaController {

	@Autowired
	LogLineDao logLineDao;

	@Autowired
	UserDao userDao;

	/**
	 * Consulta mediante una llamada JPA el log de un usuario
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/userlog", method = { RequestMethod.GET })
	public List<LogLine> newUser(@RequestParam Map<String, String> requestParams) throws Exception {
		List<LogLine> logUser = null;
		if (requestParams.get(Constantes.USERNAME) != null) {

			logUser = logLineDao.findByNameLike(requestParams.get(Constantes.USERNAME));

		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return logUser;
	}

	/**
	 * Consulta mediante una llamada JPA todos los usuarios de bd
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllUser", method = { RequestMethod.GET })
	public List<User> getAllUser(@RequestParam Map<String, String> requestParams) {
		return (List<User>) userDao.findAll();
	}

	/**
	 * Inserta mediante JPA un nuevo usuario
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */

	@ResponseBody
	@RequestMapping(value = "/register", method = { RequestMethod.POST })
	public String register(@RequestParam Map<String, String> requestParams) throws Exception {
		if (requestParams.get(Constantes.USERNAME) != null && requestParams.get(Constantes.PASS) != null) {
			User usuario = new User(requestParams.get(Constantes.USERNAME), requestParams.get(Constantes.PASS));
			userDao.save(usuario);
		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return "El usuario" + requestParams.get(Constantes.USERNAME) + " ha sido creado correctamente";
	}

	/**
	 * Actualiza mediante JPA la contraseña de un usuario
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */

	@ResponseBody
	@RequestMapping(value = "/updatePass", method = { RequestMethod.PUT })
	public String upadte(@RequestParam Map<String, String> requestParams) throws Exception {
		if (requestParams.get(Constantes.USERNAME) != null && requestParams.get(Constantes.PASS) != null) {
			User usuario = userDao.findByUserLike(requestParams.get(Constantes.USERNAME));
			usuario.setPass(requestParams.get(Constantes.PASS));
			userDao.save(usuario);

		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return "La contraseña del usuario" + requestParams.get(Constantes.USERNAME)
				+ " Ha sido actualizada correctamente";
	}

	/**
	 * Elimina un usuario mediante JPA
	 * 
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delUser", method = { RequestMethod.DELETE })
	public String delete(@RequestParam Map<String, String> requestParams) throws Exception {
		if (requestParams.get(Constantes.USERNAME) != null) {
			User usuario = userDao.findByUserLike(requestParams.get(Constantes.USERNAME));
			userDao.delete(usuario);

		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return "El usuario" + requestParams.get(Constantes.USERNAME) + " ha sido eliminado correctamente";
	}

	/**
	 * Borra todos los registros de la tabla indicada mediante JPA
	 * 
	 * @param requestParams
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/cleanTable", method = { RequestMethod.DELETE })
	public String delAllRows(@RequestParam Map<String, String> requestParams) throws Exception {
		if (requestParams.get(Constantes.TABLE_NAME) != null) {
			if (requestParams.get(Constantes.TABLE_NAME).equalsIgnoreCase(Constantes.DBUSER)) {
				userDao.deleteAll();
			} else if (requestParams.get(Constantes.TABLE_NAME).equalsIgnoreCase(Constantes.DBLOGUSER)) {
				logLineDao.deleteAll();
			}

		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return "Todos los registros de la tabla " + requestParams.get(Constantes.TABLE_NAME) + " han sido eliminados";
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	public boolean login(@RequestParam Map<String, String> requestParams) throws Exception {
		if (requestParams.get(Constantes.USERNAME) != null && requestParams.get(Constantes.PASS) != null) {
			String user = requestParams.get(Constantes.USERNAME);
			String pass = requestParams.get(Constantes.PASS);
			if (userDao.findByUserAndPassLike(user, pass) != null) {
				LogLine line = new LogLine();
				line.setName(user);
				line.setFechaLog(new Timestamp(new Date().getTime()));
				logLineDao.save(line);
				return true;

			}
		} else {
			throw new Exception(Constantes.LOS_PARAMETROS_INTRODUCIDOS_NO_SON_LOS_ESPERADOS);
		}
		return false;
	}

}

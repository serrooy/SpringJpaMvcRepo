package org.java.login.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MainServices {

	/**
	 * Conexion jdbc al h2 - Esta utilizara la configuracion especificada en el
	 * aplication.properties
	 */
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Constructor
	 */
	public MainServices() {
		super();
	}

	/**
	 * Dado un usuario y una contraseña valida si el usuario es correcto.
	 * 
	 * @param user
	 * @param pass
	 * @return
	 * @throws SQLException
	 */
	public boolean validarUser(String user, String pass) throws SQLException {
		DataSource datasource = jdbcTemplate.getDataSource();
		try (Connection con = datasource.getConnection()) {
			try (PreparedStatement st = con
					.prepareStatement("Select USER,PASS FROM PUBLIC.DBUSER WHERE USER=? AND PASS=?")) {
				st.setString(1, user);
				st.setString(2, pass);
				try (ResultSet result = st.executeQuery()) {
					if (result.next()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Inserta una nueva fila cada vez que realizamos un LOG para un usuario
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void insertUser(String user, String pass) throws SQLException {
		Integer num = null;
		DataSource datasource = jdbcTemplate.getDataSource();
		try (Connection con = datasource.getConnection()) {
			try (PreparedStatement stCon = con.prepareStatement("SELECT MAX(ID) FROM PUBLIC.DBUSER")) {
				try (ResultSet result = stCon.executeQuery()) {
					if (!result.wasNull() && result.next()) {
						num = result.getInt(1);
					}
				}
			}
			try (PreparedStatement st = con
					.prepareStatement("INSERT INTO PUBLIC.DBUSER (ID,USER,PASS) VALUES(?,?,?)")) {
				st.setInt(1, num + 1);
				st.setString(2, user);
				st.setString(3, pass);
				st.executeUpdate();
			}
		}
	}

	/**
	 * Inserta una nueva fila cada vez que realizamos un LOG para un usuario
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void insertLog(String user) throws SQLException {
		Integer num = null;
		DataSource datasource = jdbcTemplate.getDataSource();
		try (Connection con = datasource.getConnection()) {
			try (PreparedStatement stCon = con.prepareStatement("SELECT MAX(ID) FROM PUBLIC.DBLOGUSER")) {
				try (ResultSet result = stCon.executeQuery()) {
					if (!result.wasNull() && result.next()) {
						num = result.getInt(1);
					}
				}
			}
			try (PreparedStatement st = con
					.prepareStatement("INSERT INTO PUBLIC.DBLOGUSER (ID,NAME,FECHALOG) VALUES(?,?,?)")) {
				st.setInt(1, num + 1);
				st.setString(2, user);
				st.setObject(3, LocalDateTime.now());
				st.executeUpdate();
			}
		}
	}

	/**
	 * Metodo para consultar todos los log-in del usuario
	 * 
	 * @param usu
	 * @return
	 * @throws SQLException
	 */
	public List<String> consultaLog(String usu) throws SQLException {
		List<String> out = new ArrayList<>();

		DataSource datasource = jdbcTemplate.getDataSource();
		try (Connection con = datasource.getConnection()) {
			try (PreparedStatement stCon = con.prepareStatement("SELECT * FROM PUBLIC.DBLOGUSER WHERE NAME=?")) {
				stCon.setString(1, usu);
				try (ResultSet result = stCon.executeQuery()) {

					while (!result.wasNull() && result.next()) {
						String nombre = result.getString(2);
						String rawFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(result.getTimestamp(3));
						String[] arrFecha = rawFecha.split(" ");

						out.add("El usuario " + nombre + " se conecto el [" + arrFecha[0] + "] a las [" + arrFecha[1]
								+ "]");
					}

				}
			}
		}
		Collections.reverse(out);
		return out;

	}

	/**
	 * Consulta todos los usuarios de la base de datos
	 * 
	 * @param usu
	 * @return
	 * @throws SQLException
	 */
	public List<String> consultaAllUser() throws SQLException {
		List<String> out = new ArrayList<>();

		DataSource datasource = jdbcTemplate.getDataSource();
		try (Connection con = datasource.getConnection()) {
			try (PreparedStatement stCon = con.prepareStatement("SELECT USER FROM PUBLIC.DBUSER")) {
				try (ResultSet result = stCon.executeQuery()) {
					while (!result.wasNull() && result.next()) {
						String nombre = result.getString(1);
						out.add("Usuario [" + nombre + "] ");
					}

				}
			}
		}
		Collections.reverse(out);
		return out;

	}

	/**
	 * Elimina el usuario tanto de la tabla de login como sus logs
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void deleteUser(String user) throws SQLException {
		DataSource datasource = jdbcTemplate.getDataSource();
		try (Connection con = datasource.getConnection()) {
			// Consultamos si existe el usuario a eliminar
			try (PreparedStatement stCon = con.prepareStatement("SELECT * FROM PUBLIC.DBUSER WHERE USER=?")) {
				stCon.setString(1, user);
				try (ResultSet result = stCon.executeQuery()) {
					if (result.wasNull() || !result.next()) {
						throw new SQLException();
					}
				}
			}
			// Eliminamos todos los logs asignados a este usuario
			try (PreparedStatement stDelLog = con.prepareStatement("DELETE FROM PUBLIC.DBLOGUSER WHERE NAME=?")) {
				stDelLog.setString(1, user);
				stDelLog.executeUpdate();
			}

			// Eliminamos el usuario
			try (PreparedStatement stDelUser = con.prepareStatement("DELETE FROM PUBLIC.DBUSER WHERE USER=? ")) {
				stDelUser.setString(1, user);
				stDelUser.executeUpdate();

			}
		}
	}

	public void updatePass(String user, String pass) throws SQLException {
		DataSource datasource = jdbcTemplate.getDataSource();
		try (Connection con = datasource.getConnection()) {
			// Consultamos si existe el usuario a eliminar
			try (PreparedStatement stUpd = con.prepareStatement("UPDATE PUBLIC.DBUSER SET PASS=? WHERE USER=?")) {
				stUpd.setString(1, pass);
				stUpd.setString(2, user);
				stUpd.executeUpdate();
			}
		}

	}

}

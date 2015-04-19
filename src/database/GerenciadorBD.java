package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import sun.org.mozilla.javascript.internal.ast.ArrayComprehensionLoop;

public class GerenciadorBD {
	public Statement stm; //responsável por preparar e realizar consultas ao banco
	public ResultSet rs; //resultado de uma consulta passada para o statement
	private String driver = "com.mysql.jdbc.Driver"; //responsável por identificar o serviço de banco de dados
	private String caminho = "jdbc:mysql://localhost:3306/"; //responsável por localizar o banco de dados
	private String usuario = "root";
	private String senha = "";
	public Connection conn; //responsável por conectar ao banco de dados
	
	public void conectaBanco(){
		try {
			
			conn = DriverManager.getConnection(caminho, usuario, senha);
			//System.out.println("Conectado com Sucesso");
		} catch (SQLException e) {
			System.out.print("Não foi possível conectar ao banco de dados ");
			e.printStackTrace();
		}
	}
	
	public void desconectaBanco(){
		try{	
			conn.close();
		}
		catch(SQLException e){
			System.out.print("Não foi possível fechar a conexão com o banco de dados");
		}
	}
	
	/* O método a seguir é responsável por "quebrar" a pagina HTML em palavras isoladas, criando um código sql para ser possível inserir todas elas no
	 * banco de dados de uma única vez, com uma unica instrução SQL
	 */
	
	public void salvaPalavrasBD(String paginaHTML, String link){
		try{
		String[] palavra;
		String temp;
		String codPalavra;
		String codLink;
		int cont = 0;

		
		stm = conn.createStatement();		

		palavra = paginaHTML.split(" ");
		
		stm.execute("USE motordebusca");
		
		rs = stm.executeQuery("SHOW TABLE STATUS LIKE 'links'");
		rs.next();
		codLink = rs.getString("Auto_increment"); //pega o cod do proximo registro a ser inserido e salva na variável codLink

		stm.execute("INSERT INTO `motordebusca`.`links` (`cod`, `link`) VALUES (NULL, " + "'" + link + "'" + ");"); //insere no banco de dados o link que está sendo salvo
		
		
		for (int i = 0; i <= palavra.length - 1; i++) {
			temp = palavra[i].trim();
			temp = temp.replace("\\", "");
			palavra[i] = temp;
			
			if(temp.length() > 0){
				
				rs = stm.executeQuery("SELECT * FROM `palavras` WHERE `palavra` LIKE '" + palavra[i] + "'");
				
				cont = cont + 1;
				
				if(rs.next()){
					codPalavra = rs.getString("cod");
					stm.execute("INSERT INTO `motordebusca`.`links_palavras` (`cod`, `cod_link`, `cod_palavra`, `posicao`) "
							+ "VALUES (NULL, '" + codLink + "','" + codPalavra + "','" + cont + "')");
				}else{
					try{
						rs = stm.executeQuery("SHOW TABLE STATUS LIKE 'palavras'");
						rs.next();
						codPalavra = rs.getString("Auto_increment");
					
						stm.execute("INSERT INTO `motordebusca`.`palavras` (`cod`, `palavra`) VALUES (NULL, '" + palavra[i] + "')");
						stm.execute("INSERT INTO `motordebusca`.`links_palavras` VALUES (NULL, '" + codLink + "', '" + codPalavra + "', '" + cont + "')");
						}catch(SQLException e){
							System.out.println("Não foi possível inserir a palavra" + palavra[i]);
						}	
					}				
				}
			}
		}
			
		catch(SQLException e){
			System.out.println("Problema ao salvar palavras no banco de dados");
			e.printStackTrace();
		}
	}
	
	public void consultaPalavras(){
		try {
		stm = conn.createStatement();
		stm.executeQuery("USE motordebusca");
		//stm.execute("INSERT INTO `motordebusca`.`palavras` (`cod`, `palavra`) VALUES (NULL, 'banana');");
		rs = stm.executeQuery("SELECT * FROM palavras");
		
		while(rs.next()){
			String cod = rs.getString(1);
			String palavra = rs.getString(2);
			System.out.println("Cod: " + cod + " Palavra: " + palavra);
		}
		
		}catch (SQLException e) {
			System.out.print("Não foi possível retornar as palavras do banco");
			e.printStackTrace();
		}
	}	
}

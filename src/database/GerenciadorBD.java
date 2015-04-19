package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import sun.org.mozilla.javascript.internal.ast.ArrayComprehensionLoop;

public class GerenciadorBD {
	public Statement stm; //respons�vel por preparar e realizar consultas ao banco
	public ResultSet rs; //resultado de uma consulta passada para o statement
	private String driver = "com.mysql.jdbc.Driver"; //respons�vel por identificar o servi�o de banco de dados
	private String caminho = "jdbc:mysql://localhost:3306/"; //respons�vel por localizar o banco de dados
	private String usuario = "root";
	private String senha = "";
	public Connection conn; //respons�vel por conectar ao banco de dados
	
	public void conectaBanco(){
		try {
			
			conn = DriverManager.getConnection(caminho, usuario, senha);
			//System.out.println("Conectado com Sucesso");
		} catch (SQLException e) {
			System.out.print("N�o foi poss�vel conectar ao banco de dados ");
			e.printStackTrace();
		}
	}
	
	public void desconectaBanco(){
		try{	
			conn.close();
		}
		catch(SQLException e){
			System.out.print("N�o foi poss�vel fechar a conex�o com o banco de dados");
		}
	}
	
	/* O m�todo a seguir � respons�vel por "quebrar" a pagina HTML em palavras isoladas, criando um c�digo sql para ser poss�vel inserir todas elas no
	 * banco de dados de uma �nica vez, com uma unica instru��o SQL
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
		codLink = rs.getString("Auto_increment"); //pega o cod do proximo registro a ser inserido e salva na vari�vel codLink

		stm.execute("INSERT INTO `motordebusca`.`links` (`cod`, `link`) VALUES (NULL, " + "'" + link + "'" + ");"); //insere no banco de dados o link que est� sendo salvo
		
		
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
							System.out.println("N�o foi poss�vel inserir a palavra" + palavra[i]);
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
			System.out.print("N�o foi poss�vel retornar as palavras do banco");
			e.printStackTrace();
		}
	}	
}

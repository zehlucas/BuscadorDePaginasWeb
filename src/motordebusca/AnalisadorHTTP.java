package motordebusca;

public class AnalisadorHTTP {
	
	public static int getCodigoDeRespostaHTTP(String respostaHTTP){
		if(respostaHTTP != null){
			
			int codigoDeRespstaHTTP=0;
			String codigoHTTPString = respostaHTTP.substring(9, 12);
			
			codigoDeRespstaHTTP = Integer.parseInt(codigoHTTPString);
			
			if(codigoDeRespstaHTTP != 0){
				return codigoDeRespstaHTTP;
			} else {
				return -1;
			}
			
		} else {
			return -1;
		}
	}
	
	
	public static String getProtocoloDeNovaLocalizacao(String respostaHTTP){
		if(respostaHTTP != null){
			
			String protocoloDeLocalizacao;
			int ini = respostaHTTP.indexOf("ocation:") + 9;
			int fim = respostaHTTP.indexOf("://", ini);
			
			protocoloDeLocalizacao = respostaHTTP.substring(ini, fim);
			return protocoloDeLocalizacao;
					
		} else {
			return null;
		}
	}
	
	
	public static String getUrlDeNovaLocalizacao(String respostaHTTP){
		if(respostaHTTP != null){
			
			String urlDeLocalizacao;
			int ini = respostaHTTP.indexOf("ocation:");
			ini = respostaHTTP.indexOf("://", ini) + 3;
			int fim = respostaHTTP.indexOf("/", ini);
			
			urlDeLocalizacao = respostaHTTP.substring(ini, fim);
			System.out.println("urlDeLocalizacao: " + urlDeLocalizacao);
			return urlDeLocalizacao;
			
		} else {
			return null;
		}
	}
	
	
	public static String getDiretorioDeNovaLocalizacao(String respostaHTTP){
		if(respostaHTTP != null){
			
			String diretorioDeLocalizacao;
			int ini = respostaHTTP.indexOf("ocation:");
			ini = respostaHTTP.indexOf("://", ini) + 3;
			ini = respostaHTTP.indexOf("/", ini);
			int fim = respostaHTTP.indexOf("\n", ini);
			
			diretorioDeLocalizacao = respostaHTTP.substring(ini, fim);
			System.out.println("diretorioDeLocalizacao: " + diretorioDeLocalizacao);			
			return diretorioDeLocalizacao;
			
		} else {
			return null;
		}
	}
	
}

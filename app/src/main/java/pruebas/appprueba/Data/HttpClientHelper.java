package pruebas.appprueba.Data;


import android.content.Context;
import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pruebas.appprueba.Entidades.Argumento;
import pruebas.appprueba.R;

/**
 * Created by Paulo on 06/05/2016.
 */
public class HttpClientHelper {

    // OperationName es el nombre del método que llamaremos
    // params es un array de argumentos o parámetros que requiere el método como datos de entrada
    // contexto es el contexto del layout que realiza el llamado
    public static JSONArray GET(String OperationName, Argumento[] params, Context contexto) throws JSONException {
        String parametros = "?";
        JSONObject resultado = null;
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout((HttpParams) basicHttpParams, (int) 10000);
        HttpConnectionParams.setSoTimeout((HttpParams) basicHttpParams, (int) 10000);
        HttpConnectionParams.setTcpNoDelay((HttpParams) basicHttpParams, (boolean) true);

        DefaultHttpClient httpClient = new DefaultHttpClient((HttpParams)basicHttpParams);

        // Se concatenan los parámetros que irán por query string
        // Se crea el query string a partir de los argumentos enviados. Si es vacio, no lo llena
        for(int i = 0; i < params.length; i++){
            Argumento item = params[i];
            parametros += item.getKey() + "=" + item.getValue();

            if(i < params.length - 1){
                parametros += "&";
            }
        }

        try{
            // Se realiza el llamado y se obtiene el resultado. Recordemos que finalmente el resultado se
            // traduce en un archivo JSON que se manipula para leer los objetos en el resultado final
            HttpGet request = new HttpGet(contexto.getResources().getString(R.string.url_wcf) + OperationName + parametros);
            BasicResponseHandler basicResponseHandler = new BasicResponseHandler();
            resultado = new JSONObject((String)httpClient.execute((HttpUriRequest)request, (ResponseHandler)basicResponseHandler));
        }
        catch(Exception ex){
            Log.e("ConsultaMongoDB", "Error al acceder al servicio WCF de Capacitaciones. Error: " + ex.getMessage());
            throw ex;
        }
        finally {
            return resultado.getJSONArray(OperationName + "Result");
        }
    }
}
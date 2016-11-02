package pruebas.appprueba;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pruebas.appprueba.Data.HttpClientHelper;
import pruebas.appprueba.Entidades.Argumento;
import pruebas.appprueba.Entidades.Interes;

public class InteresesActivity extends AppCompatActivity {

    List<Interes> lstListadoRegistros = new ArrayList<Interes>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intereses);

        ConsultarIntereses();
    }

    // Se crea un método que realiza la consulta.
    private void ConsultarIntereses(){
        // El método que llamaremos no contiene parámetros
        Argumento[] datos = new Argumento[0];

        TareaAsync tarea = new TareaAsync();
        tarea.setDatos(datos);
        tarea.setMetodo(getResources().getString(R.string.method_consultar_intereses));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            tarea.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
        else
        {
            tarea.execute();
        }
    }

    private void CargarIntereses(){
        AdaptadorIntereses adapter = new AdaptadorIntereses(InteresesActivity.this, lstListadoRegistros);
        ListView lstListadoIntereses = (ListView) findViewById(R.id.lstIntereses);
        lstListadoIntereses.setAdapter(adapter);
    }

    // Se debe crear una clase que realice la operación asincrona en un hilo diferente al hilo de ejecución principal de la app
    public class TareaAsync extends AsyncTask<Object, Object, Object>{

        public Argumento[] getDatos() {
            return datos;
        }

        public void setDatos(Argumento[] datos) {
            this.datos = datos;
        }

        public String getMetodo() {
            return Metodo;
        }

        public void setMetodo(String metodo) {
            Metodo = metodo;
        }

        // Variable que almacena los argumentos que puede tener el método del WCF
        public Argumento[] datos;

        // Variable que contiene el nombre del método a llamar
        public String Metodo;

        protected Object doInBackground(Object... params){
            List<Object> lstObject = new ArrayList<Object>();

            try{
                JSONArray lstResultado = HttpClientHelper.GET(getMetodo(), getDatos(), InteresesActivity.this);

                for(int i=0; i < lstResultado.length(); i++){
                    JSONObject item = lstResultado.getJSONObject(i);
                    Interes registro = new Interes(Integer.valueOf(item.getString("_id")), item.getString("Intereses"));
                    lstObject.add(registro);
                }
            }
            catch(Exception ex){
                Log.e("Consulta de Intereses", ex.getMessage());
            }
            finally {
                return lstObject;
            }
        }

        public void onPreExecute(){

        }

        // Cuando finaliza la consulta, se ejecuta este método
        public void onPostExecute(Object result){
            processFinish(result);
            super.onPostExecute(result);
        }

    }

    public void processFinish(Object result){
        lstListadoRegistros = (List<Interes>) result;
        CargarIntereses();
    }

    // Se debe crear el adapter para que al momento de obtener los resultados desde el WCF se puede
    // mapear a una entidad propia de la app y se entregue el resultado al listview
    class AdaptadorIntereses extends ArrayAdapter<Interes>{

        AdaptadorIntereses(Context context, List<Interes> datos){
            super(context, R.layout.listitem_intereses, datos);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View item = convertView;
            InteresesHolder holder;

            if(item == null)
            {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                item = inflater.inflate(R.layout.listitem_intereses, null);

                // Se mapea cada item del listado con los controles del layout
                holder = new InteresesHolder();
                holder.id = (TextView) item.findViewById(R.id.txtId);
                holder.intereses = (TextView) item.findViewById(R.id.txtInteres);
                item.setTag(holder);
            } else{
                holder = (InteresesHolder)item.getTag();
            }

            // lstListadoRegistros es el listado con los resultados
            final Interes registro = lstListadoRegistros.get(position);
            holder.intereses.setText(registro.getIntereses());
            holder.id.setText(String.valueOf(registro.getId()));

            return(item);
        }
    }


    // Esto nos ayudará a tener un listview más completo
    // se crean los controles que se crearán por cada item del listview
    static class InteresesHolder{
        TextView id;
        TextView intereses;
    }
}

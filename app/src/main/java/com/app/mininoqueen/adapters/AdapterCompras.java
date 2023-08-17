package com.app.mininoqueen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.modelos.Venta;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterCompras extends RecyclerView.Adapter<AdapterCompras.ViewHolder> implements

        View.OnClickListener {

    private List<Venta> itemsPedidos;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    private Context context;

    public AdapterCompras(
            Context context,
            List<Venta> itemsCards) {

        this.inflater = LayoutInflater.from(context);
        this.itemsPedidos = itemsCards;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_compras, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Venta item = itemsPedidos.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return itemsPedidos.size();
    }

    public void setOnClickListener(View.OnClickListener lister
    ) {

        this.listener = lister;
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {
            listener.onClick(v);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textCodigo;

        //        private final TextView textDescription;
        private final TextView textAmount;

        private final TextView textVendedor;
        private final TextView textTotal;
        private final TextView textFecha;
        private final CardView cardView;

        private Button btnViewDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            textDescription = itemView.findViewById(R.id.item_title_compras);
            textCodigo = itemView.findViewById(R.id.item_codigo_compra);
            textAmount = itemView.findViewById(R.id.item_amount_compra);
            textTotal = itemView.findViewById(R.id.item_total_compra);
            textFecha = itemView.findViewById(R.id.item_fecha_compra);
            textVendedor = itemView.findViewById(R.id.item_intermediario_compra);
            cardView = itemView.findViewById(R.id.card_view_compras);
            btnViewDetail = itemView.findViewById(R.id.btn_view_compras);

            // here the listener of the button that is in the list item is configured
            btnViewDetail.setOnClickListener(v -> {
                if (buttonClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Venta pedidoTouch = itemsPedidos.get(position);

                        buttonClickListener.onButtonClicked(pedidoTouch);
                    }
                }
            });
        }

        public void setData(Venta pedido) {

//            textDescription.setText(String.format("%s", pedido.getDescri));
            textCodigo.setText(pedido.getCodigo());
            textTotal.setText(String.format("%s", pedido.getTotal()));
            String dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(pedido.getFecha().toDate());
            textFecha.setText(dateFormat);
            textVendedor.setText(pedido.getIdVendedor().get("nombre").toString());
            /**
             * El total de productos que tiene el pedido, no la cantidad por cada producto
             */
            textAmount.setText(String.format("%s", pedido.getProducto().size()));


        }


    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClicked(Venta pedido);
    }

    private OnButtonClickListener buttonClickListener;

}

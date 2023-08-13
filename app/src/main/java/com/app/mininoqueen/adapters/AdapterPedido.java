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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdapterPedido extends RecyclerView.Adapter<AdapterPedido.ViewHolder> implements

        View.OnClickListener {

    private List<Pedido> itemsPedidos;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    private Context context;

    public AdapterPedido(
            Context context,
            List<Pedido> itemsCards) {

        this.inflater = LayoutInflater.from(context);
        this.itemsPedidos = itemsCards;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_pedido, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Pedido item = itemsPedidos.get(position);
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
        private final TextView textAmount;
        private final TextView textTotal;
        private final TextView textFecha;
        private final CardView cardView;

        private Button btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCodigo = itemView.findViewById(R.id.item_codigo_pedido);
            textAmount = itemView.findViewById(R.id.item_amount_pedido);
            textTotal = itemView.findViewById(R.id.item_total_pedido);
            textFecha = itemView.findViewById(R.id.item_fecha_pedido);
            cardView = itemView.findViewById(R.id.card_view_pedido);
            btnRemove = itemView.findViewById(R.id.btn_remove_pedido);

            // here the listener of the button that is in the list item is configured
            btnRemove.setOnClickListener(v -> {
                if (buttonClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Pedido pedidoTouch = itemsPedidos.get(position);

                        buttonClickListener.onButtonClicked(pedidoTouch);
                    }
                }
            });
        }

        public void setData(Pedido pedido) {

            textCodigo.setText(pedido.getCodigo());
            textTotal.setText(String.format("%s", pedido.getTotal()));
            String dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(pedido.getFecha().toDate());
            textFecha.setText(dateFormat);

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
        void onButtonClicked(Pedido pedido);
    }

    private OnButtonClickListener buttonClickListener;

}

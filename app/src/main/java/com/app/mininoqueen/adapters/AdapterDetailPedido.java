package com.app.mininoqueen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.modelos.Product;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdapterDetailPedido extends RecyclerView.Adapter<AdapterDetailPedido.ViewHolder> implements

        View.OnClickListener {

    private List<Map<String, Object>> itemsPedidos;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    private Context context;

    public AdapterDetailPedido(
            Context context,
            List<Map<String, Object>> itemsCards) {

        this.inflater = LayoutInflater.from(context);
        this.itemsPedidos = itemsCards;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_detail_pedido, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Map<String, Object> item = itemsPedidos.get(position);
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
        private final TextView textDescription;
        private final TextView textAmount;
        private final TextView textTotal;
        private final ImageView imageView;

        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCodigo = itemView.findViewById(R.id.codeProductDetails);
            textDescription = itemView.findViewById(R.id.nameProductDetails);
            textAmount = itemView.findViewById(R.id.amountProductDetails);
            textTotal = itemView.findViewById(R.id.totalProductDetails);
            imageView = itemView.findViewById(R.id.imageProductDetails);
            cardView = itemView.findViewById(R.id.card_view_pedido);

            // here the listener of the button that is in the list item is configured
//            btnRemove.setOnClickListener(v -> {
//                if (buttonClickListener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        Map<String, Object> pedidoTouch = itemsPedidos.get(position);
//
//                        buttonClickListener.onButtonClicked(pedidoTouch);
//                    }
//                }
//            });
        }

        public void setData(Map<String, Object> item) {

            textDescription.setText(item.get("descripcion") == null ? "" : Objects.requireNonNull(item.get("descripcion")).toString());
            textCodigo.setText(item.get("codigo") == null ? "" : Objects.requireNonNull(item.get("codigo")).toString());
            textAmount.setText(item.get("cantidad") == null ? "" : Objects.requireNonNull(item.get("cantidad")).toString());
            textTotal.setText(item.get("total") == null ? "" : Objects.requireNonNull(item.get("total")).toString());

            String url = item.get("image") == null ? "" : Objects.requireNonNull(item.get("image")).toString();
            if (!url.isEmpty() && !url.equals("null")) {
                // Cargar la imagen en el ImageView usando Glide
                Glide.with(context)
                        .load(url)
                        .into(imageView);
            } else imageView.setImageResource(R.drawable.ic_launcher_background);
        }


    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClicked(Map<String, Object> itemSelected);
    }

    private OnButtonClickListener buttonClickListener;

}

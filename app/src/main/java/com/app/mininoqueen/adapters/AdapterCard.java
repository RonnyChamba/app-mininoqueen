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
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewHolder> implements

        View.OnClickListener {

    private List<Map<String, Object>> itemsCards;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    private Context context;

    public AdapterCard(
            Context context,
            List<Map<String, Object>> itemsCards) {

        this.inflater = LayoutInflater.from(context);
        this.itemsCards = itemsCards;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_card_prod, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Map<String, Object> item = itemsCards.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return itemsCards.size();
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

        private final TextView textDescription;
        private final TextView textPrice;
        private final TextView textAmount;

        private final TextView textTotal;
        private final ImageView imageProduct;

        private final CardView cardView;

        private Button btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescription = itemView.findViewById(R.id.item_title_card);
            textPrice = itemView.findViewById(R.id.item_price_card);
            imageProduct = itemView.findViewById(R.id.image_card);
            textAmount = itemView.findViewById(R.id.item_amount_card);
            textTotal = itemView.findViewById(R.id.item_total_card);
            cardView = itemView.findViewById(R.id.card_view_card);
            btnRemove = itemView.findViewById(R.id.btn_remove_card);

            // here the listener of the button that is in the list item is configured
            btnRemove.setOnClickListener(v -> {
                if (buttonClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Map<String, Object> mapData = itemsCards.get(position);

                        buttonClickListener.onButtonClicked(mapData);
                    }
                }
            });
        }

        public void setData(Map<String, Object> item) {


            textDescription.setText(item.get("descripcion").toString());
            textPrice.setText(item.get("precio").toString());
            textAmount.setText(item.get("cantidad").toString());
            textTotal.setText(item.get("total").toString());

            String url = item.get("image") == null ? "" : Objects.requireNonNull(item.get("image")).toString();

            if (!url.isEmpty() && !url.equals("null")) {
                // Cargar la imagen en el ImageView usando Glide
                Glide.with(context)
                        .load(url)
                        .into(imageProduct);
            } else imageProduct.setImageResource(R.drawable.ic_launcher_background);


        }


    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClicked(Map<String, Object> product);
    }

    private OnButtonClickListener buttonClickListener;

}

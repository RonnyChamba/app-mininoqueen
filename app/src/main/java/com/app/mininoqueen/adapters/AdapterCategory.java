package com.app.mininoqueen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.modelos.Category;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> implements

        View.OnClickListener {

    private List<Category> listData;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    private Context context;

    public AdapterCategory(
            Context context,
            List<Category> listData) {

        this.inflater = LayoutInflater.from(context);
        this.listData = listData;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_category, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Category itemCourse = listData.get(position);
        holder.setData(itemCourse);
    }

    @Override
    public int getItemCount() {
        return listData.size();
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

        // aqui se referencia los widgets

        private final TextView textTitle;
        private final TextView textTotalSubcategories;
        private final TextView textDetallesPro;

        private final CardView cardView;

        private ImageView imageViewCategory;

        private Button btnReview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.itemTitleCategory);
            textDetallesPro = itemView.findViewById(R.id.item_detalles_pro);
            textTotalSubcategories = itemView.findViewById(R.id.itemSubcCategory);
            cardView = itemView.findViewById(R.id.card_view_category);
            btnReview = itemView.findViewById(R.id.btnViewProdCate);
            imageViewCategory = itemView.findViewById(R.id.imageViewCategory);

            // here the listener of the button that is in the list item is configured
            btnReview.setOnClickListener(v -> {
                if (buttonClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Category planification = listData.get(position);
                        buttonClickListener.onButtonClicked(planification);
                    }
                }
            });
        }

        public void setData(Category category) {


            // agagrear margin inferior
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            // si el indice del item es par, se pone un color de fondo, si es impar, se pone otro color de fondo
            if (getAdapterPosition() % 2 == 0) {

                params.setMargins(5, 0, 10, 10);
            } else {

                params.setMargins(0, 0, 0, 10);
            }
            cardView.setLayoutParams(params);

            textTitle.setText(category.getCategoria());
            textTotalSubcategories.setText(String.format("%s", category.getSubcategorias().size()));

            if (category.getImagen() != null && !category.getImagen().isEmpty() && !category.getImagen().equals("null")) {

                Glide.with(context)
                        .load(category.getImagen())
                        .into(imageViewCategory);
            } else {
                imageViewCategory.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClicked(Category category);
    }

    private OnButtonClickListener buttonClickListener;

}

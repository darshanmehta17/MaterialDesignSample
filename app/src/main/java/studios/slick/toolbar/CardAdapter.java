package studios.slick.toolbar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<CardListView> cardList;
    OnItemClickListener mItemClickListener;

    public CardAdapter(List<CardListView> cardList){
        this.cardList=cardList;
    }

    @Override
    public void onBindViewHolder(CardAdapter.CardViewHolder cardViewHolder, int i) {
        CardListView cl=cardList.get(i);
        cardViewHolder.title.setText(cl.title);
        cardViewHolder.link.setText(cl.link);
        cardViewHolder.img.setImageResource(cl.image);
    }

    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mycardview,viewGroup,false);
        return (new CardViewHolder(itemView));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView title,link;
        protected ImageView img;

        public CardViewHolder(View v){
            super(v);
            v.setOnClickListener(this);
            title=(TextView)v.findViewById(R.id.myCardTitle);
            link=(TextView)v.findViewById(R.id.myCardLink);
            img=(ImageView)v.findViewById(R.id.myCardImage);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(View v,int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }
}

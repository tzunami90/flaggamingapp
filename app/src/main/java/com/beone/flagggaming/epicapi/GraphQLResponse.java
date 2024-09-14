package com.beone.flagggaming.epicapi;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GraphQLResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("Catalog")
        private Catalog catalog;

        public Catalog getCatalog() {
            return catalog;
        }
    }

    public static class Catalog {
        @SerializedName("searchStore")
        private SearchStore searchStore;

        public SearchStore getSearchStore() {
            return searchStore;
        }
    }

    public static class SearchStore {
        private List<Element> elements;

        public List<Element> getElements() {
            return elements;
        }
    }

    public static class Element {
        private String title;
        private Price price;

        public String getTitle() {
            return title;
        }

        public Price getPrice() {
            return price;
        }
    }

    public static class Price {
        private TotalPrice totalPrice;

        public TotalPrice getTotalPrice() {
            return totalPrice;
        }
    }

    public static class TotalPrice {
        private FmtPrice fmtPrice;

        public FmtPrice getFmtPrice() {
            return fmtPrice;
        }
    }

    public static class FmtPrice {
        private String discountPrice;

        public String getDiscountPrice() {
            return discountPrice;
        }
    }
}

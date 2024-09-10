package com.beone.flagggaming.epicapi;

import java.util.List;

public class GraphQLResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private Catalog catalog;

        public Catalog getCatalog() {
            return catalog;
        }

        public void setCatalog(Catalog catalog) {
            this.catalog = catalog;
        }
    }

    public static class Catalog {
        private SearchStore searchStore;

        public SearchStore getSearchStore() {
            return searchStore;
        }

        public void setSearchStore(SearchStore searchStore) {
            this.searchStore = searchStore;
        }
    }

    public static class SearchStore {
        private List<Element> elements;

        public List<Element> getElements() {
            return elements;
        }

        public void setElements(List<Element> elements) {
            this.elements = elements;
        }
    }

    public static class Element {
        private String title;
        private Price price;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }
    }

    public static class Price {
        private TotalPrice totalPrice;

        public TotalPrice getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(TotalPrice totalPrice) {
            this.totalPrice = totalPrice;
        }
    }

    public static class TotalPrice {
        private FmtPrice fmtPrice;

        public FmtPrice getFmtPrice() {
            return fmtPrice;
        }

        public void setFmtPrice(FmtPrice fmtPrice) {
            this.fmtPrice = fmtPrice;
        }
    }

    public static class FmtPrice {
        private String discountPrice;

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }
    }
}

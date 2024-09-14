package com.beone.flagggaming.epicapi;
public class GraphQLRequest {
    private String query;
    private Variables variables;

    public GraphQLRequest(String title) {
        this.query = "query searchStoreQuery( " +
                "$allowCountries: String, " +
                "$category: String, " +
                "$namespace: String, " +
                "$itemNs: String, " +
                "$sortBy: String, " +
                "$sortDir: String = \"ASC\", " +
                "$start: Int, " +
                "$releaseDate: String, " +
                "$withPrice: Boolean = true) { " +
                "Catalog { " +
                "searchStore( " +
                "allowCountries: $allowCountries, " +
                "category: $category, " +
                "count: 1000, " +
                "country: \"AR\", " +
                "keywords: \"" + title + "\", " +
                "namespace: $namespace, " +
                "itemNs: $itemNs, " +
                "sortBy: $sortBy, " +
                "sortDir: $sortDir, " +
                "releaseDate: $releaseDate, " +
                "start: $start) { " +
                "elements { " +
                "title " +
                "description " +
                "keyImages { " +
                "type " +
                "url " +
                "} " +
                "seller { " +
                "name " +
                "} " +
                "categories { " +
                "path " +
                "} " +
                "price(country: \"AR\") @include(if: $withPrice) { " +
                "totalPrice { " +
                "fmtPrice(locale: \"en-US\") { " +
                "discountPrice " +
                "} " +
                "} " +
                "} " +
                "} " +
                "} " +
                "} " +
                "} ";
        this.variables = new Variables("AR", true);
    }


    public Variables getVariables() {
        return variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }

    public static class Variables {
        private String allowCountries;
        private boolean withPrice;

        public Variables(String allowCountries, boolean withPrice) {
            this.allowCountries = allowCountries;
            this.withPrice = withPrice;
        }

        public String getAllowCountries() {
            return allowCountries;
        }

        public void setAllowCountries(String allowCountries) {
            this.allowCountries = allowCountries;
        }

        public boolean isWithPrice() {
            return withPrice;
        }

        public void setWithPrice(boolean withPrice) {
            this.withPrice = withPrice;
        }
    }
}

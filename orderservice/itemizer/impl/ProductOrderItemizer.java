@Override
public List<ProductOrderItem> itemize(OrderRequest order) {
    if (order == null) {
        throw new GeneralException("Order cannot be null");
    }

    List<ProductOrderItem> items = new ArrayList<>();
    // ...existing code...
    productOrder.getProducts().forEach(product -> {
        ProductOrderItem item = new ProductOrderItem();
        item.setId(UUID.randomUUID());
        // ...existing code...
        item.setOrderType(order.getBaseOrder().getOrderType());
        item.setCreateDate(LocalDateTime.now());
        item.setCreatedBy(KeycloakUtil.getKeycloakUsername());
        item.setLastModifiedBy(item.getCreatedBy());
        item.setUpdateDate(item.getCreateDate());

        // Filter and move characteristics with sourceType = "provisionCharacteristic"
        if (product.getCharacteristics() != null) {
            List<Characteristic> provisionCharacteristics = new ArrayList<>();
            product.getCharacteristics().removeIf(characteristic -> {
                if ("provisionCharacteristic".equals(characteristic.getSourceType())) {
                    provisionCharacteristics.add(characteristic);
                    return true;
                }
                return false;
            });
            item.setCharacteristics(provisionCharacteristics);
        }

        items.add(item);
    });

    log.info("itemizer - Order request: " + order);
    return items;
}

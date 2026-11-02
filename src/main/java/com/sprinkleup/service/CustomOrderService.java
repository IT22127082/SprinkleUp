package com.sprinkleup.service;

import com.sprinkleup.dto.CustomOrderForm;
import com.sprinkleup.exception.NotFoundException;
import com.sprinkleup.model.CustomOrder;
import com.sprinkleup.model.CustomOrderStatus;
import com.sprinkleup.model.Customer;
import com.sprinkleup.model.User;
import com.sprinkleup.repository.CustomOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

@Service
@Transactional
public class CustomOrderService extends AbstractCrudService<CustomOrder> {

    private final CustomOrderRepository customOrders;

    public CustomOrderService(CustomOrderRepository customOrders) {
        super(customOrders);
        this.customOrders = customOrders;
    }

    @Override
    protected String entityName() {
        return "Custom cake request";
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomOrder> findAll() {
        return customOrders.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<CustomOrder> findByStatus(CustomOrderStatus status) {
        return status == null ? findAll() : customOrders.findByStatusOrderByCreatedAtDesc(status);
    }

    @Transactional(readOnly = true)
    public List<CustomOrder> findByCustomer(Customer customer) {
        return customOrders.findByCustomerOrderByCreatedAtDesc(customer);
    }

    @Transactional(readOnly = true)
    public long countByStatus(CustomOrderStatus status) {
        return customOrders.countByStatus(status);
    }

    /** Agreed prices of custom cakes that are going ahead or done. */
    @Transactional(readOnly = true)
    public BigDecimal confirmedRevenue() {
        return customOrders.findByStatusIn(EnumSet.of(CustomOrderStatus.ACCEPTED, CustomOrderStatus.IN_PROGRESS, CustomOrderStatus.COMPLETED))
                .stream().map(CustomOrder::getQuotedPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** A customer can only open their own requests; anyone else's look like they don't exist. */
    @Transactional(readOnly = true)
    public CustomOrder findOwned(Long id, User user) {
        CustomOrder order = findById(id);
        if (!order.belongsTo(user)) {
            throw new NotFoundException(entityName() + " #" + id + " was not found");
        }
        return order;
    }

    // ---------- Customer side ----------

    public CustomOrder request(Customer customer, CustomOrderForm form) {
        return save(new CustomOrder(customer, form));
    }

    public CustomOrder updateRequest(Long id, User user, CustomOrderForm form) {
        CustomOrder order = findOwned(id, user);
        order.updateRequest(form);
        return save(order);
    }

    public CustomOrder accept(Long id, User user) {
        CustomOrder order = findOwned(id, user);
        order.accept();
        return save(order);
    }

    public CustomOrder cancel(Long id, User user) {
        CustomOrder order = findOwned(id, user);
        order.cancel();
        return save(order);
    }

    // ---------- Bakery side ----------

    public CustomOrder quote(Long id, BigDecimal price, String note) {
        CustomOrder order = findById(id);
        order.quote(price, note);
        return save(order);
    }

    public CustomOrder changeStatus(Long id, CustomOrderStatus status) {
        CustomOrder order = findById(id);
        order.changeStatus(status);
        return save(order);
    }
}

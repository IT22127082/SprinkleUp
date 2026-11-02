// SprinkleUp page helpers
document.addEventListener('DOMContentLoaded', () => {

    // Ask before deleting: any form with data-confirm="message"
    document.querySelectorAll('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', event => {
            if (!confirm(form.dataset.confirm)) {
                event.preventDefault();
            }
        });
    });

    // Live total on the order pages
    document.querySelectorAll('[data-order-form]').forEach(form => {
        const totalEl = form.querySelector('[data-order-total]');
        const inputs = form.querySelectorAll('input[data-price]');
        const money = new Intl.NumberFormat('en-LK', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

        const update = () => {
            let total = 0;
            inputs.forEach(input => {
                const qty = parseInt(input.value, 10) || 0;
                total += qty * parseFloat(input.dataset.price);
                input.closest('.su-pick')?.classList.toggle('su-picked', qty > 0);
            });
            if (totalEl) totalEl.textContent = money.format(total);
        };
        inputs.forEach(input => input.addEventListener('input', update));
        update();

        form.addEventListener('submit', event => {
            const anyChosen = [...inputs].some(i => (parseInt(i.value, 10) || 0) > 0);
            if (!anyChosen) {
                event.preventDefault();
                alert('Please choose at least one item.');
            }
        });
    });

    // Product form: show only the details box for the chosen product type
    const productForm = document.getElementById('productForm');
    if (productForm) {
        // a <select> on the "new" page, a hidden input on the "edit" page
        const typeInput = productForm.querySelector('[name="type"]');
        const hint = productForm.querySelector('[data-price-hint]');
        const hints = { CAKE: 'per kg', CUPCAKE: 'per piece', BREAD: 'per loaf', PASTRY: 'each' };

        const showBox = () => {
            const type = typeInput.value;
            productForm.querySelectorAll('.su-specs-box').forEach(box => {
                const active = box.dataset.type === type;
                box.hidden = !active;
                box.disabled = !active; // hidden boxes are not sent with the form
            });
            if (hint) hint.textContent = hints[type] ? '(' + hints[type] + ')' : '';
        };
        typeInput.addEventListener('change', showBox);
        showBox();
    }
});

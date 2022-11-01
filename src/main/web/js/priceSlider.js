$("#rangePrimary").ionRangeSlider({
    type: "integer",
    grid: false,
    min: $("#rangePrimary").attr('min-price'),
    max: $("#rangePrimary").attr('max-price'),
    from: $("#rangePrimary").attr('current-min-price'),
    to: $("#rangePrimary").attr('current-max-price'),
    prefix: "₴"
});
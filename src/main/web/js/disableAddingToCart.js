$(document).ready(function () {
    $('div[class=add-to-cart]').each(function () {
        let $amount = $(this).children('input').val();
        if ($amount == 0) {
            let $outOfStock = $('#outOfStock').val();
            $(this).children('button').text($outOfStock).prop('disabled', true);
        }
    })
});
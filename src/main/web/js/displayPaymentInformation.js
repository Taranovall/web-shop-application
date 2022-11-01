$(document).ready(function () {
    if ($('#error').length > 0) {
        $('#buy').prop('disabled', true)
    }

    $('input[type=radio][name=payment]').change(function () {
        if (this.id === 'card') {
            $('#buy').before("<div class=\"row mt-2\" id=\"card-info\">\n" +
                "            <div class=\"col-6\">\n" +
                "                <div>\n" +
                "                    <input type=\"text\" class=\"form-control\" name=\"expiration-date\" placeholder=\"MM/YY\"/>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"col-6\">\n" +
                "                <div>\n" +
                "                    <input type=\"text\" class=\"form-control\" name=\"cvv\" placeholder=\"CVV\"/>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"col-12 mt-2\">\n" +
                "                <div>\n" +
                "                    <input type=\"text\" class=\"form-control\" name=\"card\" placeholder=\"CARD NUMBER\"/>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>");
        } else if (this.id === 'cash') {
            $('#card-info').remove();
        }
    })
});


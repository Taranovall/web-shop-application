const validations = {
    required: function (element) {
        const isEmpty = element.val() === '';
        if (isEmpty) {
            return element.attr("placeholder") + " cannot be empty!";
        }
    },
    username: function (element) {
        const firstLetter = element.val()[0];

        const firstLetterIsUpperCase = (firstLetter === firstLetter.toUpperCase());
        const inputName = element.attr('placeholder');

        if (!firstLetterIsUpperCase) {
            return inputName + " should start from upper case letter!";
        }

        const badUsernameLength = (element.val().length < 1 || element.val().length > 15);

        if (badUsernameLength) {
            return inputName + " length should be from 1 to 15 symbols";
        }
    },
    password: function (element) {
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(element.val());

        if (!passwordRegex) {
            return "Password should contain minimum eight characters, at least one letter and one number";
        }

        const firstPassword = $('#first-password').val();
        const secondPassword = $('#second-password').val();

        if (firstPassword !== secondPassword) {
            return "Passwords aren't equal"
        }
    },
    email: function (element) {
        const emailRegex = /^[A-Za-z\d+_-]+@(.+)$/.test(element.val());

        if (!emailRegex) {
            return "Invalid email";
        }
    }
};

const errorElement = $("#show-error");

function printErrorIfExist() {
    if (errorElement.html() !== "") {
        const fieldToBeHighlighted = errorElement.attr('field');
        errorElement.css({display: "block"});
        $("#" + fieldToBeHighlighted).addClass("field-error");
    }
}
printErrorIfExist();

$('button').on('click', function (e) {
    $('form input').each(function () {
        const element = $(this),
            attr = element.attr("data-validation"),
            rules = attr ? attr.split(' ') : '';

        $.each(rules, function (i, val) {
                if ((msg = validations[val](element))) {
                    e.preventDefault();

                    element.addClass("field-error");
                    errorElement.html(msg);
                    errorElement.css({display: "block"});

                    e.stop();
                }
                element.removeClass('field-error');
                errorElement.css({display: "none"});
            }
        );
    });
});
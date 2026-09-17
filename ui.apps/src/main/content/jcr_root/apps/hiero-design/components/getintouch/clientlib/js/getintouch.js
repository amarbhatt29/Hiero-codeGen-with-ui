(function () {
    'use strict';

    var COPY_SELECTOR = '.cmp-getintouch__copy';
    var STATUS_SELECTOR = '.cmp-getintouch__copy-status';
    var RESET_DELAY = 2000;

    function isCopySupported() {
        return !!(navigator.clipboard && navigator.clipboard.writeText)
            || !!(document.queryCommandSupported && document.queryCommandSupported('copy'));
    }

    function fallbackCopy(text) {
        var textarea = document.createElement('textarea');
        textarea.value = text;
        textarea.setAttribute('readonly', '');
        textarea.style.position = 'absolute';
        textarea.style.left = '-9999px';
        document.body.appendChild(textarea);
        textarea.select();
        var succeeded = false;
        try {
            succeeded = document.execCommand('copy');
        } catch (error) {
            succeeded = false;
        }
        document.body.removeChild(textarea);
        return succeeded;
    }

    function copyValue(value) {
        if (navigator.clipboard && navigator.clipboard.writeText) {
            return navigator.clipboard.writeText(value);
        }
        return fallbackCopy(value) ? Promise.resolve() : Promise.reject(new Error('Copy command failed'));
    }

    function announce(root, message) {
        var status = root.querySelector(STATUS_SELECTOR);
        if (status) {
            status.textContent = message;
        }
    }

    function handleCopyClick(event) {
        var button = event.currentTarget;
        var root = button.closest('.cmp-getintouch');
        var value = button.getAttribute('data-copy-value');
        var copiedLabel = button.getAttribute('data-copied-label') || 'Copied';
        var copyLabel = button.getAttribute('data-copy-label') || 'Copy';

        if (!value || !root) {
            return;
        }

        copyValue(value).then(function () {
            button.textContent = copiedLabel;
            announce(root, copiedLabel);
            window.setTimeout(function () {
                button.textContent = copyLabel;
            }, RESET_DELAY);
        }).catch(function () {
            announce(root, 'Unable to copy. Please copy the value manually.');
        });
    }

    function init(root) {
        var buttons = root.querySelectorAll(COPY_SELECTOR);
        if (!isCopySupported()) {
            buttons.forEach(function (button) {
                button.setAttribute('hidden', '');
            });
            return;
        }
        buttons.forEach(function (button) {
            button.addEventListener('click', handleCopyClick);
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.cmp-getintouch').forEach(init);
    });
}());

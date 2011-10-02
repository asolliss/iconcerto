$.blockUI.defaults.css = {};
$.blockUI.defaults.overlayCSS = {};

function lockMessage(message) {
	$.blockUI.defaults.message = message;
}

function lockScreen() {
	$.blockUI();
}

function unlockScreen() {
	$.unblockUI();
}

function lockerAjaxResponse(data) {
	if (data.status == 'begin') {
		lockScreen();
	}
	else if (data.status == 'complete') {
		unlockScreen();
	}
}

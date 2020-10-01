function changeItemsPerPage(element) {
    let url = element.getAttribute('per-page-url') + element.value;
    window.document.location.href = url;
}
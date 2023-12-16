function initGallery(galleryId, dir, count) {
    const gallery = $(`#${galleryId}`);
    for (let i = 1; i <= count; i++) {
        const imageName = `image${i}.jpg`;

        const img = $(`
            <img src="${dir}${imageName}" class="d-block w-100" data-toggle="modal" data-target="#${imageName.replace(/\..*/, '')}Modal">
        `);

        gallery.append(img);

        $('body').append(`
            <div class="modal fade" id="${imageName.replace(/\..*/, '')}Modal" tabindex="-1" role="dialog" aria-labelledby="${imageName.replace(/\..*/, '')}ModalLabel" aria-hidden="true">
                <div class="modal-dialog d-flex justify-content-center align-items-center" role="document">
                    <img src="${dir}${imageName}" class="modal-content-gallery">
                </div>
            </div>
        `);
    }
}


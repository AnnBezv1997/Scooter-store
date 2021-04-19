var grid = grid || document.querySelector('vaadin-grid');
var resetGrid = function() {
    console.log("resetGrid");
    var grid = grid || document.querySelector('vaadin-grid');
    grid.items = {};
    grid.size = 0;
    grid.refreshItems();
};
HTMLImports.whenReady(function() {

    grid.size = 3;
    grid.scrollToStart();
    // code
    grid.items = function(params, callback) {
        if(params.index >= 10){
            console.log('grid Resetssss!');
            grid.items = [];
            grid.size = 20;
            grid.scrollToStart();
            // params.index = 0;

        }
        // Fetch the JSON data from a URL
        var url = 'https://randomuser.me/api?index=' + params.index + '&results=' + params.count;
        $.get(url, function(data){
            var size = grid.size;
            if (params.index + params.count == size) {
                // Requested for the final batch of data, increase the size
                size += 10;
            }
            callback(data.results, size);
            grid.footer.getCell(0, 0).content = 'Current size: ' + size;
        });

    };

});
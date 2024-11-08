// create canvas element and append it to document body
    var canvas = document.createElement('canvas');
    document.body.appendChild(canvas);

    // some hotfixes... ( ≖_≖)
    document.body.style.margin = 0;
    canvas.style.position = 'fixed';

    // to account for the canvas offset
    var rect = canvas.getBoundingClientRect();

    // get canvas 2D context and set him correct size;
    // i will go with fixed size, for adaptive full-window-sized canvas check the link
    var ctx = canvas.getContext('2d');
    //resize();
    ctx.canvas.width = 200; // 200px
    ctx.canvas.height = 200;
    ctx.fillStyle = '#000000';
    ctx.fillRect(0, 0, 200, 200);

    // last known position
    var pos = { x: 0, y: 0 };

    //window.addEventListener('resize', resize);
    document.addEventListener('mousemove', draw);
    document.addEventListener('mousedown', setPosition);
    document.addEventListener('mouseenter', setPosition);

    // new position from mouse event
    function setPosition(e) {
      pos.x = e.clientX - rect.left;
      pos.y = e.clientY - rect.top;
    }

    // resize canvas
    /*function resize() {
      ctx.canvas.width = window.innerWidth;
      ctx.canvas.height = window.innerHeight;
    }*/

    function draw(e) {
      // mouse left button must be pressed
      if (e.buttons !== 1) return;

      ctx.beginPath(); // begin

      ctx.lineWidth = 5;
      ctx.lineCap = 'round';
      //ctx.strokeStyle = '#c0392b';
      ctx.strokeStyle = '#ffffff';

      ctx.moveTo(pos.x, pos.y); // from
      setPosition(e);
      ctx.lineTo(pos.x, pos.y); // to

      ctx.stroke(); // draw it!
    }

    function resetCanvas(){
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillRect(0, 0, 200, 200);
    }

    function prepareInputVector(){
        // we first need to coarse grain the picture and turn it into 28px x 28px image
        // and then make a grayscale 28x28 input vector that we will pass to network on server

        var inputVector=[];

        var context = document.getElementById('myCanvas').getContext('2d');
        // Get the CanvasPixelArray from the given coordinates and dimensions.
        var imgd = context.getImageData(x, y, width, height);
        var pix = imgd.data;



    }
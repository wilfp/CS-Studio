

new Vue({
  el: '#app',
  data: {
    show: false
  },
  methods: {
    appear: function (el, done) {
        alert("test");
        el.innerHTML = "<div>WORKING</div>";
      done();
    },
  }
});

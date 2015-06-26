contract MultiplyAsync {

  event multiplyCallback(uint product);

  function multiply(uint a, uint b) {
    multiplyCallback(a * b);
  }

}

(function (_, Kotlin) {
  'use strict';
  var Kind_CLASS = Kotlin.Kind.CLASS;
  function AClass(x) {
    this.x = x;
  }
  AClass.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'AClass',
    interfaces: []
  };
  AClass.prototype.component1 = function () {
    return this.x;
  };
  AClass.prototype.copy_za3lpa$ = function (x) {
    return new AClass(x === void 0 ? this.x : x);
  };
  AClass.prototype.toString = function () {
    return 'AClass(x=' + Kotlin.toString(this.x) + ')';
  };
  AClass.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.x) | 0;
    return result;
  };
  AClass.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && Kotlin.equals(this.x, other.x))));
  };
  function main() {
    new AClass(5);
  }
  var package$com = _.com || (_.com = {});
  var package$thehumancolossuslab = package$com.thehumancolossuslab || (package$com.thehumancolossuslab = {});
  var package$odca = package$thehumancolossuslab.odca || (package$thehumancolossuslab.odca = {});
  package$odca.AClass = AClass;
  package$odca.main = main;
  main();
  Kotlin.defineModule('odca-kotlin', _);
  return _;
}(module.exports, require('kotlin')));

//# sourceMappingURL=odca-kotlin.js.map

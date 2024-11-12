const path = require('path');
const webpack = require('webpack');

module.exports = {
  resolve: {
    fallback: [{
      "net": false,
      global: require.resolve('global')
    }]
  },
  module: {
    rules: [
      {
        test: /\.ts$/,
        loader: 'ts-loader',
        exclude: /node_modules/
      }
    ]
  },
  plugins: [
    new webpack.DefinePlugin({
      'global': 'window'
    })
  ]
};

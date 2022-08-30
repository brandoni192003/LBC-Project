// File created by Kenzie Academy
// File updated by Brandon Januska-Wilson

const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    bookReservationPage: path.resolve(__dirname, 'src', 'pages', 'bookReservationPage.js'),
    employeePage: path.resolve(__dirname, 'src', 'pages', 'employeePage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    openPage: 'http://localhost:8080/bookReservation.html',
    // diableHostChecks, otherwise we get an error about headers and the page won't render
    disableHostCheck: true,
    contentBase: 'packaging_additional_published_artifacts',
    // overlay shows a full-screen overlay in the browser when there are compiler errors or warnings
    overlay: true,
    proxy: [
      {
        context: [
            '/reservations',
            '/trips'
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/bookReservation.html',
      filename: 'bookReservation.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
        template: './src/employee.html',
        filename: 'employee.html',
        inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    })
  ]
}

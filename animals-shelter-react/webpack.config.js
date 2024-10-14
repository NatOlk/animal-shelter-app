const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const webpack = require('webpack');

const env = Object.keys(process.env)
  .filter(key => key.startsWith('REACT_APP_'))
  .reduce((obj, key) => {
    obj[`process.env.${key}`] = JSON.stringify(process.env[key]);
    return obj;
  }, {});

module.exports = {
    entry: './src/index.js',
    devtool: 'source-map',
    cache: true,
    mode: 'development',
    output: {
        path: path.resolve(__dirname, 'built'),
        filename: 'bundle.js',
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            }
        ]
    },
    resolve: {
        extensions: ['.js', '.jsx'],
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'public/index.html'),
            filename: 'index.html',
            inject: 'body',
        }),
        new webpack.DefinePlugin(env),
    ],
    devServer: {
        port: 3000,
        static: path.join(__dirname, 'public'),
        hot: true,
        open: true,
        historyApiFallback: true
    }
};

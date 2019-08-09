import React from 'react';
import ReactDOM from 'react-dom';
import App2 from './App';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<App2 />, div);
  ReactDOM.unmountComponentAtNode(div);
});

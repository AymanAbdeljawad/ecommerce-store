import React, { createContext, useState } from 'react';
import Cookies from 'js-cookie';

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
  const [cart, setCart] = useState(JSON.parse(Cookies.get('cart') || '[]'));

  const updateCart = (newCart) => {
    setCart(newCart);
    Cookies.set('cart', JSON.stringify(newCart), { expires: 7 });
  };

  return (
    <CartContext.Provider value={{ cart, updateCart }}>
      {children}
    </CartContext.Provider>
  );
};

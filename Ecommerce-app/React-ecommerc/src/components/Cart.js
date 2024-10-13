import React, { useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { v4 as uuidv4 } from 'uuid';
import './Cart.css';

const Cart = () => {
  const [cart, setCart] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const cartData = JSON.parse(Cookies.get('cart') || '[]');
    setCart(cartData);
  }, []);

  const updateQuantity = (productId, amount) => {
    const updatedCart = cart.map(item => {
      if (item.productId === productId) {
        const newQuantity = item.quantity + amount;
        return { ...item, quantity: Math.max(newQuantity, 1) };
      }
      return item;
    });

    setCart(updatedCart);
    Cookies.set('cart', JSON.stringify(updatedCart), { expires: 7 });
  };

  const removeFromCart = (productId) => {
    const updatedCart = cart.filter(item => item.productId !== productId);
    setCart(updatedCart);
    Cookies.set('cart', JSON.stringify(updatedCart), { expires: 7 });
  };

  const handleCreateOrder = async () => {
    if (cart.length === 0) {
      alert('📦 It looks like your cart is empty. Add some products before creating your order!');
      return;
    }

    const newToken = uuidv4();
    Cookies.set('sessionToken', newToken, { expires: 7 });

    const orderData = {
      clientId: "0",
      tracingId: "0",
      errorCode: "a",
      errorDesc: "a",
      token: newToken,
      cartItemsDTO: cart.map(item => ({
        productName: item.name,
        quantity: item.quantity.toString(),
        price: item.price.toFixed(2),
        description: item.description,
      })),
    };

    try {
      const response = await axios.post('cart/api/cart/addItemsToCart', orderData);
      if (response.data.clientId === 0) {
        const id = response.data.cartDTO.id;
        const orderData1 = {
          clientId: "0",
          tracingId: "0",
          errorCode: "a",
          errorDesc: "a",
          token: newToken,
          paymentDTO: {
            cartId: id,
            token: newToken,
            totalPrice: cart.reduce((total, item) => total + item.price * item.quantity, 0),
            currency: "USD",
            quantity: cart.reduce((total, item) => total + item.quantity, 0),
            stutseOrderPayment: "1"
          }
        };

        const response1 = await axios.post('stripe/api/payment/charge', orderData1);
        window.location.href = response1.data.url;
        alert('✅ Order created successfully! Your order ID is: ' + id);
      }

      setCart([]);
      Cookies.remove('cart');
      navigate('/orders');
    } catch (error) {
        alert('❌ Order creation failed. Please try again.');
      console.error(error);
    }
  };

  const totalPrice = cart.reduce((total, item) => total + item.price * item.quantity, 0);

  return (
    <div className="container">
      <h2>Shopping Cart</h2>
      {cart.length === 0 ? (
        <p>Your cart is empty.</p>
      ) : (
        <table className="table table-bordered text-center">
          <thead className="table-light">
            <tr>
              <th>Product</th>
              <th>Description</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Total</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {cart.map(item => (
              <tr key={item.productId} className="align-middle">
                <td>
                  <img src={item.imageUrl} alt={item.name} className="img-thumbnail" style={{ width: '50px' }} />
                  {item.name}
                </td>
                <td>{item.description}</td>
                <td>${item.price}</td>
                <td>
                  <div className="d-flex align-items-center justify-content-center">
                    <button
                      className="btn btn-primary btn-lg me-2"
                      onClick={() => updateQuantity(item.productId, -1)}
                      disabled={item.quantity === 1}
                      style={{ minWidth: '70px', height: '30px', borderRadius: '8px' }}
                    >
                      <strong>-</strong>
                    </button>
                    <span className="fs-4 mx-2" style={{ minWidth: '70px', textAlign: 'center' }}>{item.quantity}</span>
                    <button
                      className="btn btn-primary btn-lg ms-2"
                      onClick={() => updateQuantity(item.productId, 1)}
                      style={{ minWidth: '70px', height: '30px', borderRadius: '8px' }}
                    >
                      <strong>+</strong>
                    </button>
                  </div>
                </td>

                <td>${item.price * item.quantity}</td>
                <td>
                  <button className="btn btn-danger btn-sm" onClick={() => removeFromCart(item.productId)}>Remove</button>
                </td>
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr>
              <td colSpan="4" className="text-end"><strong>Total Price:</strong></td>
              <td colSpan="2">${totalPrice}</td>
            </tr>
            <tr>
              <td colSpan="6">
                <button onClick={handleCreateOrder} className="btn btn-success w-100">Create Order</button>
              </td>
            </tr>
          </tfoot>
        </table>
      )}
    </div>
  );
};

export default Cart;